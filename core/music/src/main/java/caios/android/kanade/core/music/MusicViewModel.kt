package caios.android.kanade.core.music

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.model.player.PlayerState
import caios.android.kanade.core.model.player.RepeatMode
import caios.android.kanade.core.model.player.ShuffleMode
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
) : ViewModel() {

    var uiState by mutableStateOf(MusicUiState())
        private set

    init {
        viewModelScope.launch {
            combine(
                musicRepository.config,
                musicController.currentSong,
                musicController.currentQueue,
                musicController.playerState,
                musicController.playerPosition,
            ) { config, song, queue, state, position ->
                uiState.copy(
                    song = song,
                    queueItems = queue?.items ?: emptyList(),
                    queueIndex = queue?.index ?: 0,
                    progress = position,
                    state = state,
                    shuffleMode = config.shuffleMode,
                    repeatMode = config.repeatMode,
                )
            }.collect {
                uiState = it
            }
        }
    }

    fun playerEvent(event: PlayerEvent) {
        musicController.playerEvent(event)
    }
}

@Stable
data class MusicUiState(
    val song: Song? = null,
    val queueItems: List<Song> = emptyList(),
    val queueIndex: Int = 0,
    val progress: Long = 0L,
    val state: PlayerState = PlayerState.Initialize,
    val shuffleMode: ShuffleMode = ShuffleMode.OFF,
    val repeatMode: RepeatMode = RepeatMode.OFF,
) {
    val isPlaying
        get() = (state == PlayerState.Playing)

    val isLoading
        get() = (state == PlayerState.Buffering)

    val progressParent: Float
        get() {
            val duration = song?.duration ?: return 0f
            val parent = progress / duration.toDouble()

            return parent.coerceIn(0.0, 1.0).toFloat()
        }

    val progressString: String
        get() {
            val progress = progress / 1000
            val minute = progress / 60
            val second = progress % 60

            return "%02d:%02d".format(minute, second)
        }
}
