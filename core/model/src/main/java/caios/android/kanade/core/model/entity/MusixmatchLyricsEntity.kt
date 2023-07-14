package caios.android.kanade.core.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MusixmatchLyricsEntity(
    @SerialName("message")
    val message: Message,
) {
    @Serializable
    data class Message(
        @SerialName("body")
        val body: Body,
        @SerialName("header")
        val header: Header,
    ) {
        @Serializable
        data class Body(
            @SerialName("subtitle")
            val subtitle: Subtitle,
        ) {
            @Serializable
            data class Subtitle(
                @SerialName("html_tracking_url")
                val htmlTrackingUrl: String,
                @SerialName("lyrics_copyright")
                val lyricsCopyright: String,
                @SerialName("pixel_tracking_url")
                val pixelTrackingUrl: String,
                @SerialName("published_status")
                val publishedStatus: Int,
                @SerialName("restricted")
                val restricted: Int,
                @SerialName("script_tracking_url")
                val scriptTrackingUrl: String,
                @SerialName("subtitle_avg_count")
                val subtitleAvgCount: Int,
                @SerialName("subtitle_body")
                val subtitleBody: String,
                @SerialName("subtitle_id")
                val subtitleId: Int,
                @SerialName("subtitle_language")
                val subtitleLanguage: String,
                @SerialName("subtitle_language_description")
                val subtitleLanguageDescription: String,
                @SerialName("subtitle_length")
                val subtitleLength: Int,
                @SerialName("updated_time")
                val updatedTime: String,
            )
        }

        @Serializable
        data class Header(
            @SerialName("execute_time")
            val executeTime: Double,
            @SerialName("instrumental")
            val instrumental: Int,
            @SerialName("status_code")
            val statusCode: Int,
        )
    }
}
