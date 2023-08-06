package caios.android.kanade.core.repository

import caios.android.kanade.core.datastore.KanadePreferencesDataStore
import caios.android.kanade.core.model.ThemeColorConfig
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserDataRepository @Inject constructor(
    private val kanadePreferencesDataStore: KanadePreferencesDataStore,
) : UserDataRepository {

    override val userData: Flow<UserData> = kanadePreferencesDataStore.userData

    override suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        kanadePreferencesDataStore.setThemeConfig(themeConfig)
    }

    override suspend fun setThemeColorConfig(themeColorConfig: ThemeColorConfig) {
        kanadePreferencesDataStore.setThemeColorConfig(themeColorConfig)
    }

    override suspend fun setDeveloperMode(isDeveloperMode: Boolean) {
        kanadePreferencesDataStore.setDeveloperMode(isDeveloperMode)
    }

    override suspend fun setPremiumMode(isPremiumMode: Boolean) {
        kanadePreferencesDataStore.setPremiumMode(isPremiumMode)
    }

    override suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        kanadePreferencesDataStore.setUseDynamicColor(useDynamicColor)
    }

    override suspend fun setUseDynamicNormalizer(useDynamicNormalizer: Boolean) {
        kanadePreferencesDataStore.setUseDynamicNormalizer(useDynamicNormalizer)
    }

    override suspend fun setUseOneStepBack(isOneStepBack: Boolean) {
        kanadePreferencesDataStore.setUseOneStepBack(isOneStepBack)
    }

    override suspend fun setUseKeepAudioFocus(isKeepAudioFocus: Boolean) {
        kanadePreferencesDataStore.setUseKeepAudioFocus(isKeepAudioFocus)
    }

    override suspend fun setUseStopWhenTaskkill(isStopWhenTaskkill: Boolean) {
        kanadePreferencesDataStore.setUseStopWhenTaskkill(isStopWhenTaskkill)
    }

    override suspend fun setUseIgnoreShortMusic(isIgnoreShortMusic: Boolean) {
        kanadePreferencesDataStore.setUseIgnoreShortMusic(isIgnoreShortMusic)
    }

    override suspend fun setUseIgnoreNotMusic(isIgnoreNotMusic: Boolean) {
        kanadePreferencesDataStore.setUseIgnoreNotMusic(isIgnoreNotMusic)
    }
}
