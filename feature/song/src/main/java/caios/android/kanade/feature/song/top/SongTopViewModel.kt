package caios.android.kanade.feature.song.top

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Song
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
import java.util.Random
import javax.inject.Inject

@Stable
@HiltViewModel
class SongTopViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
    private val lastFmRepository: LastFmRepository,
    @Dispatcher(KanadeDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    var screenState = combine(musicRepository.config, musicRepository.updateFlag, lastFmRepository.albumDetails, ::Triple).map { (config, _, _) ->
        withContext(ioDispatcher) {
            musicRepository.fetchSongs(config)
            musicRepository.fetchAlbumArtwork()
        }

        ScreenState.Idle(
            SongTopUiState(
                songs = musicRepository.sortedSongs(config),
                sortOrder = config.songOrder,
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun onNewPlay(index: Int, queue: List<Song>) {
        musicController.playerEvent(
            PlayerEvent.NewPlay(
                index = index,
                queue = queue,
                playWhenReady = true,
            ),
        )
    }

    fun onShufflePlay(queue: List<Song>) {
        viewModelScope.launch {
            musicRepository.setShuffleMode(ShuffleMode.ON)
            musicController.playerEvent(
                PlayerEvent.NewPlay(
                    index = Random().nextInt(queue.size),
                    queue = queue,
                    playWhenReady = true,
                ),
            )
        }
    }

    suspend fun getRecentlyPlayedSongs(): List<Song> {
        musicRepository.fetchPlayHistory()
        return musicRepository.playHistory.map { it.song }
    }

    suspend fun getMostPlayedSongs(): List<Song> {
        musicRepository.fetchPlayHistory()
        return musicRepository.getPlayedCount()
            .map { it.toPair() }
            .sortedByDescending { it.second }
            .take(30)
            .map { it.first }
    }
}

@Stable
data class SongTopUiState(
    val songs: List<Song>,
    val sortOrder: MusicOrder,
)
