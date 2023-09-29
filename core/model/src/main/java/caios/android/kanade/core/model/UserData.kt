package caios.android.kanade.core.model

import androidx.compose.runtime.Stable

@Stable
data class UserData(
    val themeConfig: ThemeConfig,
    val themeColorConfig: ThemeColorConfig,
    val isAgreedPrivacyPolicy: Boolean,
    val isAgreedTermsOfService: Boolean,
    val isDynamicColor: Boolean,
    val isDeveloperMode: Boolean,
    val isPlusMode: Boolean,
    val isDynamicNormalizer: Boolean,
    val isOneStepBack: Boolean,
    val isKeepAudioFocus: Boolean,
    val isStopWhenTaskkill: Boolean,
    val isIgnoreShortMusic: Boolean,
    val isIgnoreNotMusic: Boolean,
) {
    val hasPrivilege get() = isPlusMode || isDeveloperMode

    companion object {
        fun dummy(): UserData {
            return UserData(
                themeConfig = ThemeConfig.System,
                themeColorConfig = ThemeColorConfig.Default,
                isAgreedPrivacyPolicy = false,
                isAgreedTermsOfService = false,
                isDynamicColor = true,
                isDeveloperMode = true,
                isPlusMode = false,
                isDynamicNormalizer = false,
                isOneStepBack = true,
                isKeepAudioFocus = false,
                isStopWhenTaskkill = false,
                isIgnoreShortMusic = true,
                isIgnoreNotMusic = true,
            )
        }
    }
}
