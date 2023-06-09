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
}
