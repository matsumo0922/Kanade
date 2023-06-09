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
}
