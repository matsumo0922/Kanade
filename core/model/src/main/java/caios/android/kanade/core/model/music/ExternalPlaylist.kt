package caios.android.kanade.core.model.music

data class ExternalPlaylist(
    val id: Long,
    val name: String,
    val songIds: List<Long>,
)
