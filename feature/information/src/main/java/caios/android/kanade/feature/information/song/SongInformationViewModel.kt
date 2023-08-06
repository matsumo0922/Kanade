package caios.android.kanade.feature.information.song

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.music.Volume
import caios.android.kanade.core.music.analyzer.VolumeAnalyzer
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.PlayHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongInformationViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val playHistoryRepository: PlayHistoryRepository,
    private val volumeAnalyzer: VolumeAnalyzer,
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState<SongInformationUiState>>(ScreenState.Loading)

    fun fetch(songId: Long) {
        viewModelScope.launch {
            val song = musicRepository.getSong(songId)
            val volume = song?.let { volumeAnalyzer.getVolume(it) }
            val playCount = song?.let { playHistoryRepository.playHistory(it).size } ?: 0
            val isFavorite = song?.let { musicRepository.isFavorite(song) } ?: false

            if (song != null) {
                screenState.value = ScreenState.Idle(
                    SongInformationUiState(
                        song = song,
                        volume = volume,
                        playCount = playCount,
                        isFavorite = isFavorite,
                    ),
                )
            } else {
                screenState.value = ScreenState.Error(
                    message = R.string.error_no_data,
                    retryTitle = R.string.common_close,
                )
            }
        }
    }
}

@Stable
data class SongInformationUiState(
    val song: Song,
    val volume: Volume?,
    val playCount: Int,
    val isFavorite: Boolean,
)
