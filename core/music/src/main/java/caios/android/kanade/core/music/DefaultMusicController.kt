package caios.android.kanade.core.music

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.datastore.KanadePreferencesDataStore
import caios.android.kanade.core.model.music.ControllerEvent
import caios.android.kanade.core.model.music.ControllerState
import caios.android.kanade.core.model.music.Queue
import caios.android.kanade.core.model.music.RepeatMode
import caios.android.kanade.core.model.music.ShuffleMode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DefaultMusicController @Inject constructor(
    private val player: ExoPlayer,
    private val kanadePreferencesDataStore: KanadePreferencesDataStore,
    @Dispatcher(KanadeDispatcher.IO) private val io: CoroutineDispatcher,
    @Dispatcher(KanadeDispatcher.Main) private val main: CoroutineDispatcher,
) : MusicController, Player.Listener, CoroutineScope {

    private var job: Job? = null
    private var originalItems: List<MediaItem> = emptyList()

    private val _state = MutableStateFlow<ControllerState>(ControllerState.Initialize)
    override val state = _state.asStateFlow()

    init {
        player.addListener(this)
        job = Job()

        startUpdateJob()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _state.value = ControllerState.Buffering(player.currentPosition)
            ExoPlayer.STATE_READY -> _state.value = ControllerState.Ready(player.currentMediaItem, player.currentPosition)
            else -> { /* do nothing */ }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _state.value = ControllerState.Playing(isPlaying)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        ControllerState.Ready(player.currentMediaItem, player.currentPosition)
    }

    override suspend fun onControllerEvent(event: ControllerEvent) {
        when (event) {
            ControllerEvent.Play -> onPlay()
            ControllerEvent.Pause -> onPause()
            ControllerEvent.Stop -> onStop()
            ControllerEvent.SkipToNext -> onSkipToNext()
            ControllerEvent.SkipToPrevious -> onSkipToPrevious()
            is ControllerEvent.Seek -> onSeekTo((player.duration * event.progress).toLong())
            is ControllerEvent.Shuffle -> onShuffleModeChanged(event.shuffleMode)
            is ControllerEvent.Repeat -> onRepeatModeChanged(event.repeatMode)
        }
    }

    override suspend fun restorePlayerState(
        currentItems: List<MediaItem>,
        originalItems: List<MediaItem>,
        index: Int,
        progress: Long,
        shuffleMode: ShuffleMode,
        repeatMode: RepeatMode,
    ) {
        this.originalItems = originalItems

        player.setMediaItems(currentItems, index, progress)

        player.repeatMode = when (repeatMode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }

        player.prepare()
    }

    override fun onPlayWithNewQueue(index: Int, queue: List<MediaItem>, playWhenReady: Boolean) {
        player.setMediaItems(queue, index, 0)
        player.prepare()
        player.playWhenReady = playWhenReady
    }

    override fun onPlay() {
        player.play()
        _state.value = ControllerState.Playing(true)
    }

    override fun onPause() {
        _state.value = ControllerState.Playing(false)
        player.pause()
    }

    override fun onStop() {
        stopUpdateJob()
        _state.value = ControllerState.Initialize
    }

    override fun onSkipToNext() {
        player.seekToNextMediaItem()
    }

    override fun onSkipToPrevious() {
        if (player.currentPosition < 5000) {
            player.seekToPreviousMediaItem()
        } else {
            player.seekTo(0)
        }
    }

    override fun onSeekTo(position: Long) {
        player.seekTo(position)
        _state.value = ControllerState.Progress(position)
    }

    override fun onShuffleModeChanged(shuffleMode: ShuffleMode) {
        player.shuffleModeEnabled = (shuffleMode == ShuffleMode.ON)
    }

    override fun onRepeatModeChanged(repeatMode: RepeatMode) {
        player.repeatMode = when (repeatMode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }
    }

    override fun getQueue(): Queue {
        val mediaItems = mutableListOf<MediaItem>()

        for (i in 0 until player.mediaItemCount) {
            mediaItems.add(player.getMediaItemAt(i))
        }

        return Queue(
            items = mediaItems,
            index = player.currentMediaItemIndex,
        )
    }

    private fun startUpdateJob() {
        job = createUpdateJob()
        job?.start()
    }

    private fun stopUpdateJob() {
        job?.cancel()
    }

    private fun createUpdateJob() = launch(
        context = main,
        start = CoroutineStart.LAZY,
    ) {
        while (isActive) {
            _state.value = ControllerState.Progress(player.currentPosition)
            kanadePreferencesDataStore.setLastQueueProgress(player.currentPosition)

            delay(100)
        }
    }
}
