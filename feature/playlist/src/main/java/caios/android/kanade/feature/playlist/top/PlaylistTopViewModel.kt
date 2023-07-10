package caios.android.kanade.feature.playlist.top

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.model.player.ShuffleMode
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.LastFmRepository
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistTopViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
    private val playlistRepository: PlaylistRepository,
    private val lastFmRepository: LastFmRepository,
) : ViewModel() {

    val screenState = combine(
        musicRepository.config,
        playlistRepository.data,
        lastFmRepository.albumDetails,
        ::Triple
    ).map { (config, _, _) ->
        musicRepository.fetchPlaylist(config)

        ScreenState.Idle(
            PlaylistTopUiState(
                playlists = musicRepository.sortedPlaylists(config),
                sortOrder = config.playlistOrder,
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun onNewPlay(playlist: Playlist) {
        viewModelScope.launch {
            musicRepository.setShuffleMode(ShuffleMode.OFF)

            val sorted = playlist.items.sortedBy { it.index }
            val songs = sorted.map { it.song }

            musicController.playerEvent(
                PlayerEvent.NewPlay(
                    index = 0,
                    queue = songs,
                    playWhenReady = true,
                ),
            )
        }
    }
}

@Stable
data class PlaylistTopUiState(
    val playlists: List<Playlist>,
    val sortOrder: MusicOrder,
)
