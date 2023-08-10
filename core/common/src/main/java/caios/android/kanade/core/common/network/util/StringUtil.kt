package caios.android.kanade.core.common.network.util

object StringUtil {

    fun String.toHttpsUrl(): String {
        return if (matches(Regex("^(http:).*"))) replaceFirst("http", "https") else this
    }

    fun Long.toTimeString(): String {
        return when {
            this > 3600 -> "%d:%02d:%02d".format(this / 3600, (this % 3600) / 60, this % 60)
            else -> "%02d:%02d".format(this / 60, this % 60)
        }
    }
}
