package caios.android.kanade.core.datastore

import androidx.datastore.core.DataStore
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class KanadePreferencesDataStore @Inject constructor(
    private val userPreference: DataStore<UserPreference>,
) {
    val userData = userPreference.data
        .map {
            UserData(
                useDynamicColor = true, // it.useDynamicColor,
                isDeveloperMode = it.isDeveloperMode,
                isPremiumMode = it.isPremiumMode,
                themeConfig = when (it.themeConfig) {
                    ThemeConfigProto.THEME_CONFIG_LIGHT -> ThemeConfig.Light
                    ThemeConfigProto.THEME_CONFIG_DARK -> ThemeConfig.Dark
                    else -> ThemeConfig.System
                },
            )
        }

    suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        userPreference.updateData {
            it.copy {
                this.themeConfig = when (themeConfig) {
                    ThemeConfig.Light -> ThemeConfigProto.THEME_CONFIG_LIGHT
                    ThemeConfig.Dark -> ThemeConfigProto.THEME_CONFIG_DARK
                    else -> ThemeConfigProto.THEME_CONFIG_SYSTEM
                }
            }
        }
    }

    suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        userPreference.updateData {
            it.copy {
                this.useDynamicColor = useDynamicColor
            }
        }
    }

    suspend fun setDeveloperMode(isDeveloperMode: Boolean) {
        userPreference.updateData {
            it.copy {
                this.isDeveloperMode = isDeveloperMode
            }
        }
    }

    suspend fun setPremiumMode(isPremiumMode: Boolean) {
        userPreference.updateData {
            it.copy {
                this.isPremiumMode = isPremiumMode
            }
        }
    }
}
