package caios.android.kanade.feature.artist.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.ArtistDetail
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
class ArtistDetailViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
) : ViewModel() {
    val screenState = MutableStateFlow<ScreenState<ArtistDetailUiState>>(ScreenState.Loading)

    fun fetch(artistId: Long) {
        viewModelScope.launch {
            val artist = musicRepository.getArtist(artistId)
            val artistDetail = artist?.let { musicRepository.getArtistDetail(it) }

            screenState.value = if (artist != null) {
                ScreenState.Idle(
                    ArtistDetailUiState(
                        artist = artist,
                        artistDetail = artistDetail,
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
}

@Stable
data class ArtistDetailUiState(
    val artist: Artist,
    val artistDetail: ArtistDetail?,
)
