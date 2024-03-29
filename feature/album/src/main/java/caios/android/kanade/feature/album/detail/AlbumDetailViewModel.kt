package caios.android.kanade.feature.album.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.model.player.ShuffleMode
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

@Stable
@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
) : ViewModel() {

    private var albumId: Long? = null

    private val _screenState = MutableStateFlow<ScreenState<AlbumDetailUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            musicRepository.updateFlag.collectLatest {
                if (albumId != null) {
                    fetch(albumId!!)
                }
            }
        }
    }

    fun fetch(albumId: Long) {
        viewModelScope.launch {
            this@AlbumDetailViewModel.albumId = albumId

            val album = musicRepository.getAlbum(albumId)

            _screenState.value = if (album != null) {
                ScreenState.Idle(AlbumDetailUiState(album))
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
}

@Stable
data class AlbumDetailUiState(
    val album: Album,
)
