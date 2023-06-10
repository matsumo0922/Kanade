package caios.android.kanade.core.music

import android.content.Context
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
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val musicRepository: MusicRepository,
    private val kanadePreferencesDataStore: KanadePreferencesDataStore,
    @ApplicationContext private val context: Context,
    @Dispatcher(KanadeDispatcher.IO) private val io: CoroutineDispatcher,
    @Dispatcher(KanadeDispatcher.Main) private val main: CoroutineDispatcher,
) : MusicController, Player.Listener, CoroutineScope {

    private var job: Job? = null
    private val _state = MutableStateFlow<ControllerState>(ControllerState.Initialize)

    override val state = _state.asStateFlow()

    init {
        player.addListener(this)
        job = Job()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _state.value = ControllerState.Buffering(player.currentPosition)
            ExoPlayer.STATE_READY -> _state.value = ControllerState.Ready(player.currentMediaItem)
            else -> { /* do nothing */ }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _state.value = ControllerState.Playing(isPlaying)

        if (isPlaying) {
            startUpdateJob()
        } else {
            stopUpdateJob()
        }
    }

    override suspend fun onControllerEvent(event: ControllerEvent) {
        when (event) {
            ControllerEvent.Play -> {
                startUpdateJob()
                player.play()
                _state.value = ControllerState.Playing(true)
            }
            ControllerEvent.Pause -> {
                _state.value = ControllerState.Playing(false)
                player.pause()
                stopUpdateJob()
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
            is ControllerEvent.Progress -> {
                player.seekTo(player.duration * event.progress)
            }
        }
    }

    override suspend fun restorePlayerState(items: List<MediaItem>, index: Int, progress: Long, shuffleMode: ShuffleMode, repeatMode: RepeatMode)  {
        player.setMediaItems(items, index, progress)

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
            delay(200)
        }
    }
}
