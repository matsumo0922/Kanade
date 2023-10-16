package caios.android.kanade.core.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YTMusicSearch(
    @SerialName("album")
    val album: Album?,
    @SerialName("artist")
    val artist: String?,
    @SerialName("artists")
    val artists: List<Artist>?,
    @SerialName("author")
    val author: String?,
    @SerialName("browseId")
    val browseId: String?,
    @SerialName("category")
    val category: String,
    @SerialName("duration")
    val duration: String?,
    @SerialName("duration_seconds")
    val durationSeconds: Int?,
    @SerialName("feedbackTokens")
    val feedbackTokens: FeedbackTokens?,
    @SerialName("isExplicit")
    val isExplicit: Boolean?,
    @SerialName("itemCount")
    val itemCount: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("radioId")
    val radioId: String?,
    @SerialName("resultType")
    val resultType: String,
    @SerialName("shuffleId")
    val shuffleId: String?,
    @SerialName("thumbnails")
    val thumbnails: List<Thumbnail>,
    @SerialName("title")
    val title: String?,
    @SerialName("videoId")
    val videoId: String?,
    @SerialName("videoType")
    val videoType: String?,
    @SerialName("views")
    val views: String?,
    @SerialName("year")
    val year: String?,
) {
    @Serializable
    data class Album(
        @SerialName("id")
        val id: String?,
        @SerialName("name")
        val name: String,
    )

    @Serializable
    data class Artist(
        @SerialName("id")
        val id: String?,
        @SerialName("name")
        val name: String,
    )

    @Serializable
    data class FeedbackTokens(
        @SerialName("add")
        val add: String?,
        @SerialName("remove")
        val remove: String?,
    )

    @Serializable
    data class Thumbnail(
        @SerialName("height")
        val height: Int,
        @SerialName("url")
        val url: String,
        @SerialName("width")
        val width: Int,
    )
}
