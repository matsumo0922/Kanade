package caios.android.kanade.feature.setting.top

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingTopViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val kanadeConfig: KanadeConfig,
) : ViewModel() {

    val screenState = userDataRepository.userData.map {
        ScreenState.Idle(
            SettingTopUiState(
                userData = it,
                config = kanadeConfig,
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun setThemeConfig(themeConfig: ThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setThemeConfig(themeConfig)
        }
    }

    fun setDeveloperMode(isDeveloperMode: Boolean) {
        viewModelScope.launch {
            userDataRepository.setDeveloperMode(isDeveloperMode)
        }
    }

    fun setUseDynamicColor(useDynamicColor: Boolean) {
        viewModelScope.launch {
            userDataRepository.setUseDynamicColor(useDynamicColor)
        }
    }

    fun setUseDynamicNormalizer(useDynamicNormalizer: Boolean) {
        viewModelScope.launch {
            userDataRepository.setUseDynamicNormalizer(useDynamicNormalizer)
        }
    }

    fun setOneStepBack(isOneStepBack: Boolean) {
        viewModelScope.launch {
            userDataRepository.setUseOneStepBack(isOneStepBack)
        }
    }

    fun setKeepAudioFocus(isKeepAudioFocus: Boolean) {
        viewModelScope.launch {
            userDataRepository.setUseKeepAudioFocus(isKeepAudioFocus)
        }
    }

    fun setStopWhenTaskkill(isStopWhenTaskkill: Boolean) {
        viewModelScope.launch {
            userDataRepository.setUseStopWhenTaskkill(isStopWhenTaskkill)
        }
    }

    fun setIgnoreShortMusic(isIgnoreShortMusic: Boolean) {
        viewModelScope.launch {
            userDataRepository.setUseIgnoreShortMusic(isIgnoreShortMusic)
        }
    }

    fun setIgnoreNotMusic(isIgnoreNotMusic: Boolean) {
        viewModelScope.launch {
            userDataRepository.setUseIgnoreNotMusic(isIgnoreNotMusic)
        }
    }
}

@Stable
data class SettingTopUiState(
    val userData: UserData,
    val config: KanadeConfig,
)
