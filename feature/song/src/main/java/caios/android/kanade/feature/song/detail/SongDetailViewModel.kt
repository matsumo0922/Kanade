package caios.android.kanade.feature.song.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Queue
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.model.player.ShuffleMode
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class SongDetailViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState<SongDetailUiState>>(ScreenState.Loading)

    fun fetch(songIds: List<Long>) {
        viewModelScope.launch {
            val songs = songIds.map { musicRepository.getSong(it) }
            val queue = musicController.currentQueue.value

            screenState.value = if (!songs.contains(null)) {
                ScreenState.Idle(
                    SongDetailUiState(
                        songs = songs.filterNotNull(),
                        queue = queue,
                    ),
                )
            } else {
                ScreenState.Error(
                    message = R.string.error_no_data,
                    retryTitle = R.string.common_close,
                )
            }
        }
    }

    fun onNewPlay(songs: List<Song>, index: Int) {
        musicController.playerEvent(
            PlayerEvent.NewPlay(
                index = index,
                queue = songs,
                playWhenReady = true,
            ),
        )
    }

    fun onShufflePlay(songs: List<Song>) {
        viewModelScope.launch {
            musicRepository.setShuffleMode(ShuffleMode.ON)
            musicController.playerEvent(
                PlayerEvent.NewPlay(
                    index = Random().nextInt(songs.size),
                    queue = songs,
                    playWhenReady = true,
                ),
            )
        }
    }

    fun addToQueue(songs: List<Song>, index: Int? = null) {
        viewModelScope.launch {
            musicController.addToQueue(songs, index)
        }
    }
}

@Stable
data class SongDetailUiState(
    val songs: List<Song>,
    val queue: Queue?,
)
