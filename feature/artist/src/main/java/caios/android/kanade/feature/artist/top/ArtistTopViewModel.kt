package caios.android.kanade.feature.artist.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArtistTopViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
) : ViewModel() {

    val screenState = musicRepository.config.map {
        musicRepository.fetchArtists(it)
        musicRepository.fetchArtistArtwork()
        ScreenState.Idle(musicRepository.sortedArtists(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun onNewPlay(artist: Artist) {
        musicController.playerEvent(
            PlayerEvent.NewPlay(
                index = 0,
                queue = artist.songs,
                playWhenReady = true,
            ),
        )
    }
}
