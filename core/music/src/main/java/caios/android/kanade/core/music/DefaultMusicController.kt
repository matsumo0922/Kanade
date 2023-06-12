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
            ControllerEvent.Play -> {
                player.play()
                _state.value = ControllerState.Playing(true)
            }
            ControllerEvent.Pause -> {
                _state.value = ControllerState.Playing(false)
                player.pause()
            }
            ControllerEvent.SkipToNext -> {
                player.seekToNextMediaItem()
            }
            ControllerEvent.SkipToPrevious -> {
                if (player.currentPosition < 5000) {
                    player.seekToPreviousMediaItem()
                } else {
                    player.seekTo(0)
                }
            }
            ControllerEvent.Stop -> {
                stopUpdateJob()
                _state.value = ControllerState.Initialize
            }
            is ControllerEvent.Seek -> {
                (player.duration * event.progress).toLong().also {
                    player.seekTo(it)
                    _state.value = ControllerState.Progress(it)
                }
            }
            is ControllerEvent.Shuffle -> {
                if (event.shuffleMode == ShuffleMode.ON) {
                    val queue = getQueue()
                    val remainItems = queue.items.toMutableList().apply { removeAt(queue.index) }

                    repeat(player.mediaItemCount) {
                        if (queue.index != it) player.removeMediaItem(it)
                    }

                    player.addMediaItems(remainItems.shuffled())
                } else {
                    val index = originalItems.indexOfFirst { it.mediaId == player.currentMediaItem?.mediaId }.coerceAtLeast(0)

                    player.addMediaItems(0, originalItems.subList(0, index - 1))
                    player.addMediaItems(index, originalItems.subList(index + 1, originalItems.size))
                }
            }
            is ControllerEvent.Repeat -> {
                player.repeatMode = when (event.repeatMode) {
                    RepeatMode.OFF -> Player.REPEAT_MODE_OFF
                    RepeatMode.ONE -> Player.REPEAT_MODE_ONE
                    RepeatMode.ALL -> Player.REPEAT_MODE_ALL
                }
            }
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
