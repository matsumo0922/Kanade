package caios.android.kanade.core.music

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import buildBundle
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.model.music.Queue
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.ControlAction
import caios.android.kanade.core.model.player.ControlKey
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.model.player.PlayerState
import caios.android.kanade.core.model.player.RepeatMode.Companion.toPlaybackState
import caios.android.kanade.core.model.player.ShuffleMode
import caios.android.kanade.core.repository.MusicRepository
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

interface MusicController {
    val isInitialized: StateFlow<Boolean>
    val currentSong: StateFlow<Song?>
    val currentQueue: StateFlow<Queue?>
    val playerPosition: StateFlow<Long>
    val playerState: StateFlow<PlayerState>

    fun initialize()
    fun terminate()

    fun setPlayerPlaying(isPlaying: Boolean)
    fun setPlayerState(state: Int)
    fun setPlayerItem(item: MediaMetadata)
    fun setPlayerPosition(position: Long)

    fun playerEvent(event: PlayerEvent)
}

class MusicControllerImpl @Inject constructor(
    private val musicRepository: MusicRepository,
    private val queueManager: QueueManager,
    @ApplicationContext private val context: Context,
    @Dispatcher(KanadeDispatcher.Main) private val main: CoroutineDispatcher,
    @Dispatcher(KanadeDispatcher.IO) private val io: CoroutineDispatcher,
) : MusicController {

    private var _isInitialized = MutableStateFlow(false)
    private var _currentSong = MutableStateFlow<Song?>(null)
    private var _currentQueue = MutableStateFlow<Queue?>(null)
    private var _playerPosition = MutableStateFlow(0L)
    private var _playerState = MutableStateFlow(PlayerState.Initialize)

    override val isInitialized = _isInitialized.asStateFlow()
    override val currentSong = _currentSong.asStateFlow()
    override val currentQueue = _currentQueue.asStateFlow()
    override val playerPosition = _playerPosition.asStateFlow()
    override val playerState = _playerState.asStateFlow()

    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(io + supervisorJob)

    private var isTryingConnect = false
    private var mediaBrowser: MediaBrowserCompat? = null
    private var mediaController: MediaControllerCompat? = null

    private val transportStack = mutableListOf<TransportControlEvent>()
    private val transportControls get() = mediaController?.transportControls

    private val connectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            Timber.d("Connected to MediaBrowser.")

            mediaController = mediaBrowser?.let { MediaControllerCompat(context, it.sessionToken) }
            isTryingConnect = false

            scope.launch {
                onInitialize(PlayerEvent.Initialize(false))
            }
        }

        override fun onConnectionFailed() {
            Timber.d("Connection failed to MediaBrowser.")

            mediaBrowser = null
            mediaController = null
            isTryingConnect = false
        }
    }

    private val transportLooper = scope.launch(start = CoroutineStart.LAZY) {
        while (isActive) {
            try {
                val action = transportStack.firstOrNull()

                if (action != null && transportControls != null) {
                    action.action.invoke(transportControls!!)
                    transportStack.removeFirstOrNull()
                }

                delay(100)
            } catch (e: Throwable) {
                Timber.w(e, "Cannot send transport event.")
            }
        }
    }

    init {
        scope.launch {
            queueManager.queue.collectLatest {
                _currentQueue.value = it

                musicRepository.saveQueue(
                    currentQueue = queueManager.getCurrentQueue(),
                    originalQueue = queueManager.getOriginalQueue(),
                    index = it.index,
                )
            }
        }

        scope.launch(main) {
            musicRepository.lastQueue.collect {
                if (!isInitialized.value) {
                    initialize()
                }
            }
        }
    }

    override fun initialize() {
        Timber.d("Initialize MusicController. isInitialized: ${isInitialized.value}")

        transportLooper.start()
        createConnection()
    }

    override fun terminate() {
        Timber.d("Terminate MusicController.")

        transportLooper.cancel()
        terminateConnection()
    }

    override fun setPlayerPlaying(isPlaying: Boolean) {
        when (isPlaying) {
            true -> _playerState.value = PlayerState.Playing
            false -> _playerState.value = PlayerState.Paused
        }
    }

    override fun setPlayerState(state: Int) {
        when (state) {
            ExoPlayer.STATE_BUFFERING -> _playerState.value = PlayerState.Buffering
            ExoPlayer.STATE_READY -> _playerState.value = PlayerState.Ready
        }

        _isInitialized.value = true
    }

    override fun setPlayerItem(item: MediaMetadata) {
        _currentSong.value = queueManager.getCurrentSong()
    }

    override fun setPlayerPosition(position: Long) {
        _playerPosition.value = position

        scope.launch {
            musicRepository.saveProgress(position)
        }
    }

    override fun playerEvent(event: PlayerEvent) {
        scope.launch {
            Timber.d("playerEvent: $event")

            when (event) {
                is PlayerEvent.Initialize -> onInitialize(event)
                is PlayerEvent.NewPlay -> onNewPlay(event)
                is PlayerEvent.Play -> onPlay(event)
                is PlayerEvent.Pause -> onPause(event)
                is PlayerEvent.PauseTransient -> onPauseTransient(event)
                is PlayerEvent.Stop -> onStop(event)
                is PlayerEvent.SkipToNext -> onSkipToNext(event)
                is PlayerEvent.SkipToPrevious -> onSkipToPrevious(event)
                is PlayerEvent.Seek -> onSeek(event)
                is PlayerEvent.Repeat -> onRepeatModeChanged(event)
                is PlayerEvent.Shuffle -> onShuffleModeChanged(event)
                is PlayerEvent.Dack -> onDack(event)
            }
        }
    }

    private suspend fun onNewPlay(event: PlayerEvent.NewPlay) {
        val config = musicRepository.config.first()
        val originalQueue = event.queue
        val currentQueue = if (config.shuffleMode == ShuffleMode.ON) originalQueue.shuffled() else originalQueue

        queueManager.build(
            currentQueue = currentQueue,
            originalQueue = originalQueue,
            index = event.index,
        )

        val args = buildBundle {
            putBoolean(ControlKey.PLAY_WHEN_READY, event.playWhenReady)
        }

        event.transport { sendCustomAction(ControlAction.NEW_PLAY, args) }
    }

    private fun onPlay(event: PlayerEvent.Play) {
        event.transport { play() }
    }

    private fun onPause(event: PlayerEvent.Pause) {
        event.transport { pause() }
    }

    private fun onPauseTransient(event: PlayerEvent.PauseTransient) {
        event.transport { sendCustomAction(ControlAction.PAUSE_TRANSIENT, null) }
    }

    private fun onStop(event: PlayerEvent.Stop) {
        event.transport { stop() }
    }

    private fun onSkipToNext(event: PlayerEvent.SkipToNext) {
        event.transport { skipToNext() }
    }

    private fun onSkipToPrevious(event: PlayerEvent.SkipToPrevious) {
        event.transport { skipToPrevious() }
    }

    private fun onSeek(event: PlayerEvent.Seek) {
        val duration = currentSong.value?.duration ?: 0
        val position = (duration * event.progress).toLong()

        event.transport { seekTo(position) }
    }

    private fun onRepeatModeChanged(event: PlayerEvent.Repeat) {
        event.transport { setRepeatMode(event.repeatMode.toPlaybackState()) }
    }

    private fun onShuffleModeChanged(event: PlayerEvent.Shuffle) {
        queueManager.setShuffleMode(event.shuffleMode)
    }

    private fun onDack(event: PlayerEvent.Dack) {
        val args = buildBundle {
            putBoolean(ControlKey.IS_ENABLED, event.isEnabled)
        }

        event.transport { sendCustomAction(ControlAction.DACK, args) }
    }

    private suspend fun onInitialize(event: PlayerEvent.Initialize) {
        if (isInitialized.value) return
        if (queueManager.getCurrentSong() != null) return

        val lastQueue = musicRepository.lastQueue.first()
        val args = buildBundle {
            putBoolean(ControlKey.PLAY_WHEN_READY, event.playWhenReady)
            putLong(ControlKey.PROGRESS, lastQueue.progress)
        }

        queueManager.build(
            currentQueue = lastQueue.currentItems.mapNotNull { musicRepository.getSong(it) },
            originalQueue = lastQueue.originalItems.mapNotNull { musicRepository.getSong(it) },
            index = lastQueue.index,
        )

        event.transport { sendCustomAction(ControlAction.INITIALIZE, args) }
    }

    private fun PlayerEvent.transport(action: MediaControllerCompat.TransportControls.() -> Unit) {
        transportStack.add(TransportControlEvent(this, action))
    }

    private fun createConnection() {
        if (isTryingConnect) return

        isTryingConnect = true

        mediaBrowser = MediaBrowserCompat(
            context,
            ComponentName(context, MusicService::class.java),
            connectionCallback,
            null,
        )

        mediaBrowser?.connect()
    }

    private fun terminateConnection() {
        mediaBrowser?.disconnect()

        mediaBrowser = null
        mediaController = null
    }

    private data class TransportControlEvent(
        val event: PlayerEvent,
        val action: MediaControllerCompat.TransportControls.() -> Unit,
    )
}
