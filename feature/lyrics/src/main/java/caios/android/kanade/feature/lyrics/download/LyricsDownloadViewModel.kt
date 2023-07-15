package caios.android.kanade.feature.lyrics.download

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.LyricsRepository
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.di.LyricsKugou
import caios.android.kanade.core.repository.di.LyricsMusixmatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LyricsDownloadViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    @LyricsKugou private val kugouLyrics: LyricsRepository,
    @LyricsMusixmatch private val musixmatchLyrics: LyricsRepository,
) : ViewModel() {
    val screenState = MutableStateFlow<ScreenState<LyricsDownloadUiState>>(ScreenState.Loading)

    fun fetch(songId: Long) {
        viewModelScope.launch {
            screenState.value = ScreenState.Loading

            val song = musicRepository.getSong(songId)
            val lyrics = song?.let { kugouLyrics.get(it) }

            screenState.value = if (song != null) {
                ScreenState.Idle(
                    LyricsDownloadUiState(
                        song = song,
                        lyrics = lyrics,
                    )
                )
            } else {
                ScreenState.Error(
                    message = R.string.error_no_data,
                    retryTitle = R.string.common_close,
                )
            }
        }
    }
}

@Stable
data class LyricsDownloadUiState(
    val song: Song,
    val lyrics: Lyrics?,
)
