package caios.android.kanade.feature.playlist.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.model.player.ShuffleMode
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState<PlaylistDetailUiState>>(ScreenState.Loading)

    init {
        viewModelScope.launch {
            combine(musicRepository.config, musicRepository.updateFlag, playlistRepository.data, ::Triple).collect { (config, _, data) ->
                musicRepository.fetchPlaylist(config)

                if (screenState.value is ScreenState.Idle) {
                    val uiState = screenState.value as ScreenState.Idle<PlaylistDetailUiState>
                    val playlistId = uiState.data.playlist.id
                    val playlist = data.find { it.id == playlistId }

                    screenState.value = if (playlist != null) {
                        ScreenState.Idle(uiState.data.copy(playlist = playlist))
                    } else {
                        ScreenState.Error(
                            message = R.string.error_no_data,
                            retryTitle = R.string.common_close,
                        )
                    }
                }
            }
        }
    }

    fun fetch(playlistId: Long) {
        viewModelScope.launch {
            screenState.value = ScreenState.Loading

            val playlist = musicRepository.getPlaylist(playlistId)

            screenState.value = if (playlist != null) {
                ScreenState.Idle(PlaylistDetailUiState(playlist))
            } else {
                ScreenState.Error(
                    message = R.string.error_no_data,
                    retryTitle = R.string.common_close,
                )
            }
        }
    }

    fun onNewPlay(songs: List<Song>, index: Int) {
        viewModelScope.launch {
            musicRepository.setShuffleMode(ShuffleMode.OFF)
            musicController.playerEvent(
                PlayerEvent.NewPlay(
                    index = index,
                    queue = songs,
                    playWhenReady = true,
                ),
            )
        }
    }

    fun onMoveItem(playlist: Playlist, fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            musicRepository.moveItemInPlaylist(playlist, fromIndex, toIndex)
        }
    }

    fun onDeleteItem(playlist: Playlist, index: Int) {
        viewModelScope.launch {
            musicRepository.removeFromPlaylist(playlist, index)
        }
    }
}

@Stable
data class PlaylistDetailUiState(
    val playlist: Playlist,
)
