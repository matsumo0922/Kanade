package caios.android.kanade.core.model.music

import kotlinx.serialization.Serializable

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
    )
}