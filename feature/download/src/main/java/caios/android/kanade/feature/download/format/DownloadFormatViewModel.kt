package caios.android.kanade.feature.download.format

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.download.VideoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DownloadFormatViewModel @Inject constructor() : ViewModel() {

    val screenState = MutableStateFlow<ScreenState<DownloadFormatUiState>>(ScreenState.Loading)

    fun fetchVideoInfo(videoInfo: VideoInfo) {
        screenState.value = ScreenState.Idle(DownloadFormatUiState(videoInfo))
    }
}

@Stable
data class DownloadFormatUiState(
    val videoInfo: VideoInfo,
)
