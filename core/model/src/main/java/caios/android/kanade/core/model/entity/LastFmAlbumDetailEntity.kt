package caios.android.kanade.core.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LastFmAlbumDetailEntity(
    @SerialName("album") val album: Album,
) {
    @Serializable
    data class Album(
        @SerialName("artist") val artist: String,
        @SerialName("image") val images: List<Image>,
        @SerialName("listeners") val listeners: String,
        @SerialName("mbid") val mbid: String? = null,
        @SerialName("name") val name: String,
        @SerialName("playcount") val playcount: String,
        @SerialName("tags") val tags: Tags,
        @SerialName("tracks") val tracks: Tracks,
        @SerialName("url") val url: String,
    ) {
        @Serializable
        data class Image(
            @SerialName("size") val size: String,
            @SerialName("#text") val text: String,
        )

        @Serializable
        data class Tags(
            @SerialName("tag") val tag: List<Tag>,
        ) {
            @Serializable
            data class Tag(
                @SerialName("name") val name: String,
                @SerialName("url") val url: String,
            )
        }

        @Serializable
        data class Tracks(
            @SerialName("track") val track: List<Track>,
        ) {
            @Serializable
            data class Track(
                @SerialName("@attr") val attr: Attr,
                @SerialName("name") val name: String,
                @SerialName("url") val url: String,
            ) {
                @Serializable
                data class Attr(
                    @SerialName("rank") val rank: Int,
                )
            }
        }
    }
}
