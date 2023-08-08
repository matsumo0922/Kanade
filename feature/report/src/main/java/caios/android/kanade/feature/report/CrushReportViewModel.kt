package caios.android.kanade.feature.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CrushReportViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {

    val screenState = userDataRepository.userData.map {
        ScreenState.Idle(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )
}
