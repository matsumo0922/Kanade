package caios.android.kanade.feature.album.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState<AlbumDetailUiState>>(ScreenState.Loading)

    fun fetch(albumId: Long) {
        viewModelScope.launch {
            val album = musicRepository.getAlbum(albumId)

            screenState.value = if (album != null) {
                ScreenState.Idle(AlbumDetailUiState(album))
            } else {
                ScreenState.Error(message = R.string.error_no_data)
            }
        }
    }

    fun onNewPlay(songs: List<Song>, index: Int) {
        musicController.playerEvent(
            PlayerEvent.NewPlay(
                index = index,
                queue = songs,
                playWhenReady = true,
            )
        )
    }
}

@Stable
data class AlbumDetailUiState(
    val album: Album,
)