package caios.android.kanade.feature.lyrics.download

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.datastore.TokenPreference
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LyricsDownloadViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val kanadeConfig: KanadeConfig,
    private val tokenPreference: TokenPreference,
    @LyricsKugou private val kugouLyrics: LyricsRepository,
    @LyricsMusixmatch private val musixmatchLyrics: LyricsRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<LyricsDownloadUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    fun fetch(songId: Long) {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading

            val song = musicRepository.getSong(songId)
            val lyrics = song?.let { kugouLyrics.get(it) }
            val token = tokenPreference.get(TokenPreference.KEY_MUSIXMATCH) ?: if (kanadeConfig.isDebug) kanadeConfig.musixmatchApiKey else null

            _screenState.value = if (song != null) {
                ScreenState.Idle(
                    LyricsDownloadUiState(
                        song = song,
                        lyrics = lyrics,
                        token = token,
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

    fun download(song: Song, isUseMusixmatch: Boolean, token: String?) {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading

            val lyrics = kotlin.runCatching {
                if (isUseMusixmatch && token != null) {
                    tokenPreference.set(TokenPreference.KEY_MUSIXMATCH, token)
                    musixmatchLyrics.lyrics(song)
                } else {
                    kugouLyrics.lyrics(song)
                }
            }.getOrNull()

            _screenState.value = ScreenState.Idle(
                LyricsDownloadUiState(
                    song = song,
                    lyrics = lyrics,
                    token = token,
                    state = if (lyrics != null) {
                        LyricsDownloadUiState.State.Downloaded
                    } else {
                        LyricsDownloadUiState.State.Error
                    },
                ),
            )
        }
    }
}

@Stable
data class LyricsDownloadUiState(
    val song: Song,
    val lyrics: Lyrics?,
    val token: String?,
    val state: State = State.Idle,
) {
    enum class State {
        Idle, Downloaded, Error
    }
}
