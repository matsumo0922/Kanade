package caios.android.kanade.feature.equalizer

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.datastore.EqualizerPreference
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Equalizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    private val equalizerPreference: EqualizerPreference,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<EqualizerUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            _screenState.value = ScreenState.Idle(
                EqualizerUiState(
                    equalizerPreference.getEqualizer()
                )
            )
        }
    }
}

@Stable
data class EqualizerUiState(
    val equalizer: Equalizer,
)
