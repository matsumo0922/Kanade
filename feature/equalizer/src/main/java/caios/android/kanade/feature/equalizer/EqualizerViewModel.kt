package caios.android.kanade.feature.equalizer

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.datastore.EqualizerPreference
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Equalizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    private val equalizerPreference: EqualizerPreference,
) : ViewModel() {

    val screenState = equalizerPreference.data.map {
        ScreenState.Idle(EqualizerUiState(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun updatePreset(preset: Equalizer.Preset) {
        viewModelScope.launch {
            equalizerPreference.setPreset(preset)
        }
    }

    fun updateBand(band: Equalizer.Band, value: Float) {
        viewModelScope.launch {
            equalizerPreference.setHz(band.hz, value)
        }
    }

    fun updateBassBoost(value: Float) {
        viewModelScope.launch {
            equalizerPreference.setBassBoost(value)
        }
    }
}

@Stable
data class EqualizerUiState(
    val equalizer: Equalizer,
)
