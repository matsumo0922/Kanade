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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportPlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val externalPlaylistRepository: ExternalPlaylistRepository,
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState<ExportPlaylistUiState>>(ScreenState.Loading)

    fun fetch(playlistId: Long) {
        val playlist = playlistRepository.get(playlistId)

        if (playlist != null) {
            screenState.value = ScreenState.Idle(ExportPlaylistUiState(playlist))
        } else {
            screenState.value = ScreenState.Error(
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
