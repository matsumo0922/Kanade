package caios.android.kanade.core.model.music

import java.time.LocalDateTime

data class PlayHistory(
    val id: Long,
    val song: Song,
    val playedAt: LocalDateTime,
)
