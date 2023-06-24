package caios.android.kanade.feature.album.top

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AlbumTopViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
) : ViewModel() {

    val screenState = musicRepository.config.map {
        musicRepository.fetchAlbums(it)
        musicRepository.fetchAlbumArtwork()

        ScreenState.Idle(
            AlbumTopUiState(
                albums = musicRepository.sortedAlbums(it),
                sortOrder = it.albumOrder,
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun onNewPlay(album: Album) {
        musicController.playerEvent(
            PlayerEvent.NewPlay(
                index = 0,
                queue = album.songs,
                playWhenReady = true,
            ),
        )
    }
}

@Stable
data class AlbumTopUiState(
    val albums: List<Album>,
    val sortOrder: MusicOrder,
)
