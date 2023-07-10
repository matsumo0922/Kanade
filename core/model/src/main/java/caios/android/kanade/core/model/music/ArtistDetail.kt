package caios.android.kanade.core.model.music

data class ArtistDetail(
    val data: Artist,
    val mbid: String?,
    val url: String,
    val imageUrl: String?,
    val tags: List<Tag>,
    val similarArtists: List<SimilarArtist>,
    val biography: String?,
) {
    @kotlinx.serialization.Serializable
    data class SimilarArtist(
        val name: String,
        val url: String,
        val imageUrl: String?,
    )
}
