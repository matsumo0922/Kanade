package caios.android.kanade.core.music

import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.datastore.KanadePreferencesDataStore
import caios.android.kanade.core.model.music.ControllerEvent
import caios.android.kanade.core.model.music.ControllerState
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.music.toMediaItem
import caios.android.kanade.core.repository.MusicRepository
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
    @Dispatcher(KanadeDispatcher.IO) private val dispatcher: CoroutineDispatcher,
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
            ExoPlayer.STATE_READY -> _state.value = ControllerState.Ready(player.duration)
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
                _state.value = ControllerState.Playing(true)
            }
            ControllerEvent.Pause -> {
                stopUpdateJob()
                _state.value = ControllerState.Playing(false)
            }
            ControllerEvent.SkipToNext -> {
            }
            ControllerEvent.SkipToPrevious -> TODO()
        }
    }

    override fun onPlayFromMediaId(index: Int, queue: List<Song>, playWhenReady: Boolean) {
        val mediaItems = queue.map { it.toMediaItem(null) }
    }

    private fun startUpdateJob() {
        job = createUpdateJob()
        job?.start()
    }

    private fun stopUpdateJob() {
        job?.cancel()
    }

    private fun createUpdateJob() = launch(
        context = dispatcher,
        start = CoroutineStart.LAZY,
    ) {
        while (isActive) {
            _state.value = ControllerState.Progress(player.currentPosition)
            delay(200)
        }
    }
}
