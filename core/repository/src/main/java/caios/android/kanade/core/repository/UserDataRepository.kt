package caios.android.kanade.core.repository

import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setThemeConfig(themeConfig: ThemeConfig)
    suspend fun setUseDynamicColor(useDynamicColor: Boolean)
    suspend fun setDeveloperMode(isDeveloperMode: Boolean)
    suspend fun setPremiumMode(isPremiumMode: Boolean)
}
