package caios.android.kanade.feature.album.top

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.model.player.ShuffleMode
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.LastFmRepository
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumTopViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
    private val lastFmRepository: LastFmRepository,
    @Dispatcher(KanadeDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val screenState = combine(musicRepository.config, musicRepository.updateFlag, lastFmRepository.albumDetails, ::Triple).map { (config, _, _) ->
        withContext(ioDispatcher) {
            musicRepository.fetchAlbums(config)
            musicRepository.fetchAlbumArtwork()
        }

        ScreenState.Idle(
            AlbumTopUiState(
                albums = musicRepository.sortedAlbums(config),
                sortOrder = config.albumOrder,
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun onNewPlay(album: Album) {
        viewModelScope.launch {
            musicRepository.setShuffleMode(ShuffleMode.OFF)
            musicController.playerEvent(
                PlayerEvent.NewPlay(
                    index = 0,
                    queue = album.songs,
                    playWhenReady = true,
                ),
            )
        }
    }
}

@Stable
data class AlbumTopUiState(
    val albums: List<Album>,
    val sortOrder: MusicOrder,
)
