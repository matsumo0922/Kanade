package caios.android.kanade.core.model.music

data class Artist(
    val artist: String,
    val artistId: Long,
    val albums: List<Album>,
    val artwork: Artwork,
) {
    val songs: List<Song>
        get() = albums.flatMap { it.songs }

    val duration: Long
        get() = albums.sumOf { it.duration }

    companion object {
        fun dummy(id: String = ""): Artist {
            return Artist(
                artist = "CAIOS$id",
                artistId = -1,
                albums = Album.dummies(5),
                artwork = Artwork.Internal("${id}Artist"),
            )
        }

        fun dummies(count: Int): List<Artist> {
            return (0 until count).map { dummy(it.toString()) }
        }
    }
}
