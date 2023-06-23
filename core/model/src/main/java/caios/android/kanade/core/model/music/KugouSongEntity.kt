package caios.android.kanade.music.lyrics.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KugouSongEntity(
    @SerialName("companys") val companys: String,
    @SerialName("errcode") val errcode: Int,
    @SerialName("errmsg") val errmsg: String,
    @SerialName("expire") val expire: Int,
    @SerialName("has_complete_right") val hasCompleteRight: Int,
    @SerialName("info") val info: String,
    @SerialName("keyword") val keyword: String,
    @SerialName("proposal") val proposal: String,
    @SerialName("status") val status: Int,
    @SerialName("ugc") val ugc: Int,
    @SerialName("ugccount") val ugccount: Int,
    @SerialName("candidates") val candidates: List<Candidate>,
) {
    @Serializable
    data class Candidate(
        @SerialName("id") val id: String,
        @SerialName("score") val score: Int,
        @SerialName("accesskey") val accessKey: String,
    )
}
