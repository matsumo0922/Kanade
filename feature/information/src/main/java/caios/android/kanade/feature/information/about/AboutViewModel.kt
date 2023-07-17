package caios.android.kanade.feature.information.about

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.Version
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val kanadeConfig: KanadeConfig,
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState<AboutUiState>>(
        ScreenState.Idle(
            AboutUiState(
                versions = emptyList(),
                config = kanadeConfig,
            ),
        ),
    )
}

@Stable
data class AboutUiState(
    val versions: List<Version>,
    val config: KanadeConfig,
)
