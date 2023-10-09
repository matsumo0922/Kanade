package caios.android.kanade.feature.setting.ytmusic

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.entity.YTMusicOAuthCode
import caios.android.kanade.core.music.YTMusic
import caios.android.kanade.core.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YTMusicLoginViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val ytMusic: YTMusic
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<YTMusicLoginUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            _screenState.value = ytMusic.getOAuthCode().fold(
                onSuccess = {
                    ScreenState.Idle(
                        YTMusicLoginUiState(
                            isReadyToAuth = false,
                            oauthCode = it,
                        ),
                    )
                },
                onFailure = {
                    ScreenState.Error(
                        message = R.string.error_no_data,
                        retryTitle = R.string.common_close,
                    )
                }
            )
        }
    }

    fun readyToAuth() {
        (screenState.value as? ScreenState.Idle)?.also {
            _screenState.value = ScreenState.Idle(it.data.copy(isReadyToAuth = true))
        }
    }

    suspend fun getOAuthToken(oauthCode: YTMusicOAuthCode): Boolean {
        return ytMusic.getOAuthToken(oauthCode).onSuccess {
            userDataRepository.setEnableYTMusic(true)
        }.isSuccess
    }
}

@Stable
data class YTMusicLoginUiState(
    val oauthCode: YTMusicOAuthCode,
    val isReadyToAuth: Boolean,
)
