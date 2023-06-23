package caios.android.kanade.core.music

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Queue
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.model.player.PlayerState
import caios.android.kanade.core.model.player.RepeatMode
import caios.android.kanade.core.model.player.ShuffleMode
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    var uiState by mutableStateOf(MusicUiState())
        private set

    init {
        fetch()

        viewModelScope.launch {
            combine(
                userDataRepository.userData,
                musicRepository.config,
                musicController.currentSong,
                musicController.currentQueue,
                musicController.playerState,
                musicController.playerPosition,
            ) { data ->
                val userData = data[0] as UserData
                val config = data[1] as MusicConfig
                val song = data[2] as Song?
                val queue = data[3] as Queue?
                val state = data[4] as PlayerState
                val position = data[5] as Long

                uiState.copy(
                    userData = userData,
                    song = song,
                    lyrics = song?.let { musicRepository.getLyrics(it) },
                    queueItems = queue?.items ?: emptyList(),
                    queueIndex = queue?.index ?: 0,
                    progress = position,
                    state = state,
                    shuffleMode = config.shuffleMode,
                    repeatMode = config.repeatMode,
                )
            }.collect {
                uiState = it
            }
        }
    }

    fun fetch() {
        viewModelScope.launch {
            musicRepository.fetchArtists()
            musicRepository.fetchAlbums()
            musicRepository.fetchSongs()
            musicRepository.fetchArtistArtwork()
            musicRepository.fetchAlbumArtwork()
        }
    }

    fun playerEvent(event: PlayerEvent) {
        musicController.playerEvent(event)
    }

    suspend fun fetchLyrics(song: Song) {
        musicRepository.fetchLyrics(song)
    }

    fun addToQueue(songs: List<Song>, index: Int? = null) {
        musicController.addToQueue(songs, index)
    }
}

@Stable
data class MusicUiState(
    val userData: UserData? = null,
    val song: Song? = null,
    val lyrics: Lyrics? = null,
    val queueItems: List<Song> = emptyList(),
    val queueIndex: Int = 0,
    val progress: Long = 0L,
    val state: PlayerState = PlayerState.Initialize,
    val shuffleMode: ShuffleMode = ShuffleMode.OFF,
    val repeatMode: RepeatMode = RepeatMode.OFF,
) {
    val isPlaying
        get() = (state == PlayerState.Playing)

    val isLoading
        get() = (state == PlayerState.Buffering)

    val progressParent: Float
        get() {
            val duration = song?.duration ?: return 0f
            val parent = progress / duration.toDouble()

            return parent.coerceIn(0.0, 1.0).toFloat()
        }

    val progressString: String
        get() {
            val progress = progress / 1000
            val minute = progress / 60
            val second = progress % 60

            return "%02d:%02d".format(minute, second)
        }

    companion object {
        @Composable
        fun UserData.isDarkMode(): Boolean {
            return when (themeConfig) {
                ThemeConfig.Light -> false
                ThemeConfig.Dark -> true
                else -> isSystemInDarkTheme()
            }
        }
    }
}
