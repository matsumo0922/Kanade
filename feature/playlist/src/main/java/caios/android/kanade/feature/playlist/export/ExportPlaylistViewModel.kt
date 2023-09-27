package caios.android.kanade.feature.playlist.export

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.repository.ExternalPlaylistRepository
import caios.android.kanade.core.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class ExportPlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val externalPlaylistRepository: ExternalPlaylistRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<ExportPlaylistUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    fun fetch(playlistId: Long) {
        val playlist = playlistRepository.get(playlistId)

        _screenState.value = if (playlist != null) {
            ScreenState.Idle(ExportPlaylistUiState(playlist))
        } else {
            ScreenState.Error(
                message = R.string.error_no_data,
                retryTitle = R.string.common_close,
            )
        }
    }

    fun export(playlist: Playlist) {
        viewModelScope.launch {
            externalPlaylistRepository.export(playlist)
        }
    }
}

@Stable
data class ExportPlaylistUiState(
    val playlist: Playlist,
)
