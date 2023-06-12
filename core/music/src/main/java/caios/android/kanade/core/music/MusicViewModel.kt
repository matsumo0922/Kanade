package caios.android.kanade.core.music

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.music.ControllerEvent
import caios.android.kanade.core.model.music.ControllerState
import caios.android.kanade.core.model.music.RepeatMode
import caios.android.kanade.core.model.music.ShuffleMode
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.music.toMediaItem
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
): ViewModel() {

    private var isInitializedPlayer = false
    private val config = musicRepository.config.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    var uiState: MusicUiState by mutableStateOf(MusicUiState())

    init {
        viewModelScope.launch {
            combine(musicRepository.lastQueue, musicRepository.config, musicRepository.songs, ::Triple).collect { (lastQueue, config, songs) ->
                if (!isInitializedPlayer) {
                    val currentItems = lastQueue.currentItems.mapNotNull { songId -> songs.find { it.id == songId }?.toMediaItem() }
                    val originalItems = lastQueue.originalItems.mapNotNull { songId -> songs.find { it.id == songId }?.toMediaItem() }

                    musicController.restorePlayerState(
                        currentItems = currentItems,
                        originalItems = originalItems,
                        index = lastQueue.index,
                        progress = lastQueue.progress,
                        shuffleMode = config.shuffleMode,
                        repeatMode = config.repeatMode,
                    )

                    isInitializedPlayer = true
                }
            }
        }

        viewModelScope.launch {
            combine(musicController.state, musicRepository.songs, ::Pair).collectLatest { (state, songs) ->
                uiState = when (state) {
                    ControllerState.Initialize -> {
                        MusicUiState()
                    }
                    is ControllerState.Buffering -> {

                        uiState.copy(
                            isPlaying = false,
                            progressParent = state.progress.toProgressParent(uiState.song?.duration ?: 0),
                            progressString = state.progress.toProgressString(),
                        )
                    }
                    is ControllerState.Playing -> {
                        uiState.copy(
                            isPlaying = state.isPlaying,
                        )
                    }
                    is ControllerState.Progress -> {
                        uiState.copy(
                            progressParent = state.progress.toProgressParent(uiState.song?.duration ?: 0),
                            progressString = state.progress.toProgressString(),
                        )
                    }
                    is ControllerState.Ready -> {
                        val queue = musicController.getQueue()
                        val song = songs.find { song -> song.id.toString() == state.mediaItem?.mediaId }

                        Timber.d("Ready: ${song?.title}")

                        uiState.copy(
                            song = song,
                            queueItems = queue.items.mapNotNull { item -> songs.find { it.id.toString() == item.mediaId } },
                            queueIndex = queue.index,
                            progressParent = state.progress.toProgressParent(uiState.song?.duration ?: 0),
                            progressString = state.progress.toProgressString(),
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            musicRepository.config.collect {
                uiState = uiState.copy(
                    shuffleMode = it.shuffleMode,
                    repeatMode = it.repeatMode,
                )
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            musicController.onControllerEvent(ControllerEvent.Stop)
        }
    }

    fun onControllerEvent(event: ControllerEvent) {
        viewModelScope.launch {
            musicController.onControllerEvent(event)
        }
    }

    fun onPlayWithNewQueue(index: Int, queue: List<Song>, playWhenReady: Boolean) {
        viewModelScope.launch {
            var songs = queue

            musicRepository.saveQueue(songs.map { it.id }, index, false)

            if (config.value?.shuffleMode == ShuffleMode.ON) {
                songs = songs.shuffled()
                musicRepository.saveQueue(songs.map { it.id }, index, true)
            }

            val mediaItems = songs.map { it.toMediaItem() }

            musicController.onPlayWithNewQueue(index, mediaItems, playWhenReady)
        }
    }

    private fun Long.toProgressParent(duration: Long): Float {
        return if (duration == 0L) {
            0f
        } else {
            (this / duration.toDouble()).coerceIn(0.0, 1.0).toFloat()
        }
    }

    private fun Long.toProgressString(): String {
        val second = this / 1000
        val minute = second / 60
        val hour = minute / 60

        return if (hour > 0) {
            "%02d:%02d:%02d".format(hour, minute % 60, second % 60)
        } else {
            "%02d:%02d".format(minute, second % 60)
        }
    }
}

@Stable
data class MusicUiState(
    val isPlaying: Boolean = false,
    val song: Song? = null,
    val queueItems: List<Song> = emptyList(),
    val queueIndex: Int = 0,
    val progressParent: Float = 0f,
    val progressString: String = "00:00",
    val shuffleMode: ShuffleMode = ShuffleMode.OFF,
    val repeatMode: RepeatMode = RepeatMode.OFF,
)