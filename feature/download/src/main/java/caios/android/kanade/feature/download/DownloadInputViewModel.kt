package caios.android.kanade.feature.download

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DownloadInputViewModel @Inject constructor() : ViewModel() {

}

@Stable
data class DownloadInputUiState(
    val tmp: String,
)
