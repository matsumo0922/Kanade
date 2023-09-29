package caios.android.kanade.feature.setting.developer

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class SettingDeveloperViewModel @Inject constructor(
    private val kanadeConfig: KanadeConfig,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    fun submitPassword(password: String): Boolean {
        if (password == kanadeConfig.developerPassword) {
            viewModelScope.launch { userDataRepository.setDeveloperMode(true) }
            return true
        }

        return false
    }
}
