package caios.android.kanade.core.model.music

data class AlbumDetail(
    val data: Album,
    val mbid: String?,
    val imageUrl: String?,
    val tags: List<FmTag>,
    val tracks: List<Track>,
) {
    @kotlinx.serialization.Serializable
    data class Track(
        val track: Int,
        val musicName: String,
        val url: String,
    )
}
