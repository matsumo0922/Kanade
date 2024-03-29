package caios.android.kanade.feature.artist.top

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.LastFmRepository
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Stable
@HiltViewModel
class ArtistTopViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
    private val lastFmRepository: LastFmRepository,
    @Dispatcher(KanadeDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val screenState = combine(musicRepository.config, musicRepository.updateFlag, lastFmRepository.artistDetails, ::Triple).map { (config, _, _) ->
        withContext(ioDispatcher) {
            musicRepository.fetchArtistArtwork()
            musicRepository.fetchArtists(config)
        }

        ScreenState.Idle(
            ArtistTopUiState(
                artists = musicRepository.sortedArtists(config),
                sortOrder = config.artistOrder,
            ),
        )
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

@Stable
data class ArtistTopUiState(
    val artists: List<Artist>,
    val sortOrder: MusicOrder,
)
