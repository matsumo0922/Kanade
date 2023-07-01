package caios.android.kanade.core.model.music

data class Playlist(
    val id: Long,
    val name: String,
    val items: Set<PlaylistItem>,
    val isSystemPlaylist: Boolean = false,
)

data class PlaylistItem(
    val id: Long,
    val song: Song,
    val index: Int,
)
