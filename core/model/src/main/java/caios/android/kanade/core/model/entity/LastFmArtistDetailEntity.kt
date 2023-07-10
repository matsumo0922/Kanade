package caios.android.kanade.core.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LastFmArtistDetailEntity(
    @SerialName("artist") val artist: Artist,
) {
    @Serializable
    data class Artist(
        @SerialName("bio") val biography: Biography,
        @SerialName("image") val images: List<Image>,
        @SerialName("name") val name: String,
        @SerialName("mbid") val mbid: String? = null,
        @SerialName("similar") val similar: Similar,
        @SerialName("stats") val stats: Stats,
        @SerialName("tags") val tags: LastFmAlbumDetailEntity.Album.Tags,
        @SerialName("url") val url: String,
    ) {
        @Serializable
        data class Biography(
            @SerialName("content") val content: String,
            @SerialName("links") val links: Links,
            @SerialName("published") val published: String,
            @SerialName("summary") val summary: String,
        ) {
            @Serializable
            data class Links(
                @SerialName("link") val link: Link,
            ) {
                @Serializable
                data class Link(
                    @SerialName("href") val href: String,
                    @SerialName("rel") val rel: String,
                    @SerialName("#text") val text: String,
                )
            }
        }

        @Serializable
        data class Image(
            @SerialName("size") val size: String,
            @SerialName("#text") val text: String,
        )

        @Serializable
        data class Similar(
            @SerialName("artist") val artist: List<Artist>,
        ) {
            @Serializable
            data class Artist(
                @SerialName("image") val image: List<Image>,
                @SerialName("name") val name: String,
                @SerialName("url") val url: String,
            ) {
                @Serializable
                data class Image(
                    @SerialName("size") val size: String,
                    @SerialName("#text") val text: String,
                )
            }
        }

        @Serializable
        data class Stats(
            @SerialName("listeners") val listeners: String,
            @SerialName("playcount") val playcount: String,
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
    }
}
