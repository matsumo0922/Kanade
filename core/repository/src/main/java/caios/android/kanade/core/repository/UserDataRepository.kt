package caios.android.kanade.core.repository

import caios.android.kanade.core.model.ThemeColorConfig
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setThemeConfig(themeConfig: ThemeConfig)
    suspend fun setThemeColorConfig(themeColorConfig: ThemeColorConfig)
    suspend fun setUseDynamicColor(useDynamicColor: Boolean)
    suspend fun setDeveloperMode(isDeveloperMode: Boolean)
    suspend fun setPremiumMode(isPremiumMode: Boolean)
    suspend fun setUseDynamicNormalizer(useDynamicNormalizer: Boolean)
    suspend fun setUseOneStepBack(isOneStepBack: Boolean)
    suspend fun setUseKeepAudioFocus(isKeepAudioFocus: Boolean)
    suspend fun setUseStopWhenTaskkill(isStopWhenTaskkill: Boolean)
    suspend fun setUseIgnoreShortMusic(isIgnoreShortMusic: Boolean)
    suspend fun setUseIgnoreNotMusic(isIgnoreNotMusic: Boolean)
}
