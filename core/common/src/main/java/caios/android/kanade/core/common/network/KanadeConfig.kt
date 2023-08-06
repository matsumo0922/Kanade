package caios.android.kanade.core.common.network

data class KanadeConfig(
    val applicationId: String,
    val buildType: String,
    val versionCode: String,
    val versionName: String,
    val isDebug: Boolean,
    val developerPassword: String,
    val lastFmApiKey: String,
    val lastFmApiSecret: String,
    val musixmatchApiKey: String,
) {
    companion object {
        fun dummy(): KanadeConfig {
            return KanadeConfig(
                applicationId = "caios.android.kanade",
                buildType = "release",
                versionCode = "223",
                versionName = "1.4.21",
                isDebug = false,
                developerPassword = "1919191919",
                lastFmApiKey = "1919191919",
                lastFmApiSecret = "1919191919",
                musixmatchApiKey = "1919191919",
            )
        }
    }
}
