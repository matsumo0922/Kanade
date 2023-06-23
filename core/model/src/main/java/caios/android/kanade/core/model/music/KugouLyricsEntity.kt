package caios.android.kanade.music.lyrics.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KugouLyricsEntity(
    @SerialName("charset")
    val charset: String,
    @SerialName("content")
    val content: String,
    @SerialName("contenttype")
    val contenttype: Int,
    @SerialName("error_code")
    val errorCode: Int,
    @SerialName("fmt")
    val fmt: String,
    @SerialName("info")
    val info: String,
    @SerialName("_source")
    val source: String,
    @SerialName("status")
    val status: Int,
)
