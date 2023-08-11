package caios.android.kanade.feature.download.format

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.download.VideoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DownloadFormatViewModel @Inject constructor() : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<DownloadFormatUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    fun fetchVideoInfo(videoInfo: VideoInfo) {
        _screenState.value = ScreenState.Idle(DownloadFormatUiState(videoInfo))
    }

    fun updateSavePath(savePath: String) {
        val state = screenState.value

        if (state is ScreenState.Idle) {
            _screenState.value = ScreenState.Idle(state.data.copy(savePath = savePath))
        }
    }
}

@Stable
data class DownloadFormatUiState(
    val videoInfo: VideoInfo,
    val savePath: String = "",
)
