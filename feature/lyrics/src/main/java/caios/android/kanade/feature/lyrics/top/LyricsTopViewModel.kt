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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@Stable
@HiltViewModel
class LyricsTopViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    @LyricsMusixmatch private val lyricsRepository: LyricsRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<LyricsTopUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            lyricsRepository.data.collect { data ->
                Timber.d("lyricsRepository.data.collect")

                val state = screenState.value

                if (state is ScreenState.Idle) {
                    val lyrics = data.find { state.data.song.id == it.songId }

                    if (lyrics != null) {
                        _screenState.value = ScreenState.Idle(
                            LyricsTopUiState(
                                song = state.data.song,
                                lyrics = lyrics,
                            ),
                        )
                    }
                }
            }
        }
    }

    fun fetch(songId: Long) {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading
            _screenState.value = kotlin.runCatching {
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

    fun save(lyrics: Lyrics) {
        viewModelScope.launch {
            lyricsRepository.save(lyrics)
        }
    }
}

@Stable
data class LyricsTopUiState(
    val song: Song,
    val lyrics: Lyrics?,
)
