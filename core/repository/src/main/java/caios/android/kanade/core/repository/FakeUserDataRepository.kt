package caios.android.kanade.core.repository

import caios.android.kanade.core.datastore.KanadePreferencesDataStore
import caios.android.kanade.core.model.ThemeConfig
import javax.inject.Inject

class FakeUserDataRepository @Inject constructor(
    private val kanadePreferencesDataStore: KanadePreferencesDataStore
): UserDataRepository {

    override val userData = kanadePreferencesDataStore.userData

    override suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        kanadePreferencesDataStore.setThemeConfig(themeConfig)
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
}