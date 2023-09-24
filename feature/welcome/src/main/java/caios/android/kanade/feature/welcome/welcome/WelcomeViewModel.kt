package caios.android.kanade.feature.welcome.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    fun setAgreedPrivacyPolicy() {
        viewModelScope.launch {
            userDataRepository.setAgreedPrivacyPolicy(true)
        }
    }

    fun setAgreedTermsOfService() {
        viewModelScope.launch {
            userDataRepository.setAgreedTermsOfService(true)
        }
    }
}
