package caios.android.kanade.feature.playlist.add

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class AddToPlaylistViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
) : ViewModel() {

    val screenState = musicRepository.config.map {
        musicRepository.fetchSongs(it)
        musicRepository.fetchPlaylist(it)

        ScreenState.Idle(
            AddToPlaylistUiState(
                songs = musicRepository.sortedSongs(it),
                playlists = musicRepository.sortedPlaylists(it),
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun register(playlist: Playlist, songs: List<Song>) {
        viewModelScope.launch {
            musicRepository.addToPlaylist(playlist, songs)
        }
    }
}

@Stable
data class AddToPlaylistUiState(
    val songs: List<Song>,
    val playlists: List<Playlist>,
)
