package caios.android.kanade.core.model.music

import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class Lyrics(
    val songId: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val lines: List<Line>,
    val isSynchronized: Boolean,
) {
    val text
        get() = lines.joinToString(separator = "\n") { it.content }

    val lrc
        get() = if (isSynchronized) {
            lines.joinToString(separator = "\n") { "[${it.lrcTime}]${it.content}" }
        } else {
            lines.joinToString(separator = "\n") { it.content }
        }

    val optimalDurationMillis
        get() = lines.maxOfOrNull { it.startAt + it.duration } ?: 0L

    init {
        for (line in lines) {
            require(line.startAt >= 0) { "startAt in the LyricsLine must >= 0" }
            require(line.duration >= 0) { "durationMillis in the LyricsLine >= 0" }
        }
    }

    @Serializable
    data class Line(
        val content: String,
        val startAt: Long,
        val duration: Long,
    ) {
        val lrcTime
            get() = when {
                (startAt / 1000) >= 60 -> {
                    val minutes = (startAt / 1000) / 60
                    val second = (startAt / 1000) % 60
                    val mills = (startAt % 1000) / 10

                    String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, second, mills)
                }

                else -> {
                    val second = (startAt / 1000) % 60
                    val mills = (startAt % 1000) / 10

                    String.format(Locale.getDefault(), "00:%02d:%02d", second, mills)
                }
            }
    }
}
