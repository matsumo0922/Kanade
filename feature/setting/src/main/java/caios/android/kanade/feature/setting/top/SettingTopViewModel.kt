package caios.android.kanade.feature.setting.top

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.music.YTMusic
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.UserDataRepository
import com.yausername.youtubedl_android.YoutubeDL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Stable
@HiltViewModel
class SettingTopViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val userDataRepository: UserDataRepository,
    private val kanadeConfig: KanadeConfig,
    private val ytMusic: YTMusic,
    @Dispatcher(KanadeDispatcher.IO) private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    val screenState = userDataRepository.userData.map {
        fetch()

        ScreenState.Idle(
            SettingTopUiState(
                userData = it,
                config = kanadeConfig,
                isYTMusicInitialized = ytMusic.isInitialized(),
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    private fun fetch() {
        viewModelScope.launch(dispatcher) {
            musicRepository.clear()
            musicRepository.fetchSongs()
            musicRepository.fetchArtists()
            musicRepository.fetchAlbums()
            musicRepository.fetchPlaylist()
            musicRepository.fetchAlbumArtwork()
            musicRepository.fetchArtistArtwork()
            musicRepository.refresh()
        }
    }

    fun setDeveloperMode(isDeveloperMode: Boolean) {
        viewModelScope.launch {
            userDataRepository.setDeveloperMode(isDeveloperMode)
        }
    }

    fun setEnableYTMusic(isEnable: Boolean) {
        viewModelScope.launch {
            userDataRepository.setEnableYTMusic(isEnable)
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

    fun removeYTMusicToken() {
        viewModelScope.launch {
            ytMusic.removeToken()
            setEnableYTMusic(false)
        }
    }

    suspend fun updateYoutubeDL(context: Context): String? = withContext(dispatcher) {
        with(YoutubeDL.getInstance()) {
            updateYoutubeDL(context)
            version(context)
        }
    }
}

@Stable
data class SettingTopUiState(
    val userData: UserData,
    val config: KanadeConfig,
    val isYTMusicInitialized: Boolean,
)
