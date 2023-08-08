package caios.android.kanade.feature.download

import android.util.Patterns
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadInputViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(DownloadInputUiState())
        private set

    fun updateUrl(input: String) {
        uiState = uiState.copy(
            url = input,
            error = if (Patterns.WEB_URL.matcher(input).matches()) null else R.string.download_input_error_invalid_url,
        )
    }

    fun fetchInfo() {
        viewModelScope.launch {
            uiState = uiState.copy(state = State.Loading)
        }
    }
}

@Stable
data class DownloadInputUiState(
    val state: State = State.Idle,
    val url: String = "",
    val error: Int? = null,
)
