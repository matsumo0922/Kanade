package caios.android.kanade.feature.information.about

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.datastore.PreferenceVersion
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.Version
import caios.android.kanade.core.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class AboutViewModel @Inject constructor(
    kanadeConfig: KanadeConfig,
    userDataRepository: UserDataRepository,
    preferenceVersion: PreferenceVersion,
) : ViewModel() {

    val screenState = userDataRepository.userData.map {
        ScreenState.Idle(
            AboutUiState(
                userData = it,
                config = kanadeConfig,
                versions = preferenceVersion.get(),
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )
}

@Stable
data class AboutUiState(
    val versions: List<Version>,
    val userData: UserData,
    val config: KanadeConfig,
)
