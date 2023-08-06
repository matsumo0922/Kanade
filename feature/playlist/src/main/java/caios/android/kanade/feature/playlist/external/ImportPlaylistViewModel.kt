package caios.android.kanade.feature.playlist.external

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.ExternalPlaylist
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.repository.ExternalPlaylistRepository
import caios.android.kanade.core.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportPlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val externalPlaylistRepository: ExternalPlaylistRepository,
) : ViewModel() {

    val screenState = playlistRepository.data.map { playlists ->
        val externalPlaylists = externalPlaylistRepository.getExternalPlaylists()

        if (externalPlaylists.isNotEmpty()) {
            ScreenState.Idle(
                ImportPlaylistUiState(
                    playlists = playlists,
                    externalPlaylists = externalPlaylists,
                ),
            )
        } else {
            ScreenState.Error(
                message = R.string.playlist_import_error_no_data,
                retryTitle = R.string.common_close,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun import(externalPlaylist: ExternalPlaylist) {
        viewModelScope.launch {
            externalPlaylistRepository.import(externalPlaylist.id)
        }
    }
}

@Stable
data class ImportPlaylistUiState(
    val playlists: List<Playlist>,
    val externalPlaylists: List<ExternalPlaylist>,
)
