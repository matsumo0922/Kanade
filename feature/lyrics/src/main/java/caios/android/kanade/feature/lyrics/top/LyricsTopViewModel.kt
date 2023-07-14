package caios.android.kanade.feature.lyrics.top

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.LyricsRepository
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.di.LyricsMusixmatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LyricsTopViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    @LyricsMusixmatch private val lyricsRepository: LyricsRepository,
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState<LyricsTopUiState>>(ScreenState.Loading)

    fun fetch(songId: Long) {
        viewModelScope.launch {
            screenState.value = ScreenState.Loading
            screenState.value = kotlin.runCatching {
                val song = musicRepository.getSong(songId)!!
                val lyrics = lyricsRepository.get(song)

                LyricsTopUiState(song, lyrics)
            }.fold(
                onSuccess = { ScreenState.Idle(it) },
                onFailure = {
                    ScreenState.Error(
                        message = R.string.error_no_data,
                        retryTitle = R.string.common_close,
                    )
                },
            )
        }
    }

    fun fetchLyrics(song: Song) {
        viewModelScope.launch {
            screenState.value = ScreenState.Loading
            screenState.value = kotlin.runCatching {
                lyricsRepository.lyrics(song)
            }.fold(
                onSuccess = {
                    ScreenState.Idle(
                        LyricsTopUiState(
                            song = song,
                            lyrics = it
                        )
                    )
                },
                onFailure = {
                    ScreenState.Idle(
                        LyricsTopUiState(
                            song = song,
                            lyrics = null,
                        )
                    )
                },
            )
        }
    }
}

@Stable
data class LyricsTopUiState(
    val song: Song,
    val lyrics: Lyrics?,
)
