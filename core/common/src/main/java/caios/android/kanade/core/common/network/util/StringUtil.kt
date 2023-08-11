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

    fun Float.toFileSizeString(): String {
        val mega = 1024f * 1024f
        val giga = mega * 1024f

        return when {
            this > giga -> "%.2f GB".format(this / giga)
            this > mega -> "%.2f MB".format(this / mega)
            else -> "%.2f KB".format(this / 1024f)
        }
    }

    fun Float.toBitRateString(): String {
        return when {
            this > 1024f -> "%.2f Mbps".format(this / 1024f)
            else -> "%.2f Kbps".format(this)
        }
    }
}
