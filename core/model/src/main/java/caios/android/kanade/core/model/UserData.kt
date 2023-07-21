package caios.android.kanade.core.model

data class UserData(
    val themeConfig: ThemeConfig,
    val isDynamicColor: Boolean,
    val isDeveloperMode: Boolean,
    val isPremiumMode: Boolean,
    val isDynamicNormalizer: Boolean,
    val isOneStepBack: Boolean,
    val isKeepAudioFocus: Boolean,
    val isStopWhenTaskkill: Boolean,
    val isIgnoreShortMusic: Boolean,
    val isIgnoreNotMusic: Boolean,
) {
    companion object {
        fun dummy(): UserData {
            return UserData(
                themeConfig = ThemeConfig.System,
                isDynamicColor = true,
                isDeveloperMode = true,
                isPremiumMode = false,
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
