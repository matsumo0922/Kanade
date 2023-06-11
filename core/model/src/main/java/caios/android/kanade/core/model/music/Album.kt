package caios.android.kanade.core.model.music

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

    companion object {
        fun dummy(id: String = ""): Album {
            return Album(
                album = "テストアルバム$id",
                albumId = -1,
                songs = Song.dummies(5),
                artwork = Artwork.Internal("${id}Album"),
            )
        }

        fun dummies(count: Int): List<Album> {
            return (0 until count).map { dummy(it.toString()) }
        }
    }
}
