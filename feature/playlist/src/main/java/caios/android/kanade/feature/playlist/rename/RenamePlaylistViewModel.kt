package caios.android.kanade.feature.playlist.rename

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RenamePlaylistViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState<RenamePlaylistUiState>>(ScreenState.Loading)

    fun fetch(playlistId: Long) {
        viewModelScope.launch {
            screenState.value = ScreenState.Loading
            screenState.value = kotlin.runCatching {
                musicRepository.fetchPlaylist()
                musicRepository.playlists
            }.fold(
                onSuccess = { playlists ->
                    ScreenState.Idle(
                        RenamePlaylistUiState(
                            playlist = playlists.first { it.id == playlistId },
                            playlists = playlists,
                        ),
                    )
                },
                onFailure = { ScreenState.Error(R.string.error_no_data) },
            )
        }
    }

    fun rename(playlist: Playlist, name: String) {
        viewModelScope.launch {
            musicRepository.renamePlaylist(playlist, name)
        }
    }
}

@Stable
data class RenamePlaylistUiState(
    val playlist: Playlist,
    val playlists: List<Playlist>
)
