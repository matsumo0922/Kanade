package caios.android.kanade.core.model.music

data class Playlist(
    val id: Long,
    val name: String,
    val items: Set<PlaylistItem>,
    val isSystemPlaylist: Boolean = false,
) {
    val songs: List<Song>
        get() = items.map { it.song }

    companion object {
        fun dummy(id: Long = 0, size: Int = 10): Playlist {
            return Playlist(
                id = id,
                name = "プレイリスト$id",
                items = PlaylistItem.dummies(size).toSet(),
            )
        }

        fun dummies(count: Int): List<Playlist> {
            return (0 until count).map { dummy(it.toLong()) }
        }
    }
}

data class PlaylistItem(
    val id: Long,
    val song: Song,
    val index: Int,
) {
    companion object {
        fun dummy(id: Long = 0): PlaylistItem {
            return PlaylistItem(
                id = id,
                song = Song.dummy(id),
                index = id.toInt(),
            )
        }

        fun dummies(count: Int): List<PlaylistItem> {
            return (0 until count).map { dummy(it.toLong()) }
        }
    }
}
