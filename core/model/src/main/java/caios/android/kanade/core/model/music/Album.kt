package caios.android.kanade.core.model.music

import java.time.LocalDateTime

data class Album(
    val album: String,
    val albumId: Long,
    val songs: List<Song>,
    val artwork: Artwork,
) {
    val artist: String
        get() = songs.firstOrNull()?.artist ?: ""

    val artistId: Long
        get() = songs.firstOrNull()?.artistId ?: 0

    val year: Int
        get() = songs.firstOrNull()?.year ?: 0

    val duration: Long
        get() = songs.sumOf { it.duration }

    val addedDate: LocalDateTime
        get() = songs.maxOf { it.addedDate }

    companion object {
        fun dummy(id: Long = 0): Album {
            return Album(
                album = "テストアルバム$id",
                albumId = id,
                songs = Song.dummies(5),
                artwork = Artwork.Internal("${id}Album"),
            )
        }

        fun dummies(count: Int): List<Album> {
            return (0 until count).map { dummy(it.toLong()) }
        }
    }
}
