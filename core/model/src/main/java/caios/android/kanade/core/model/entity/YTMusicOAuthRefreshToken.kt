package caios.android.kanade.core.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YTMusicOAuthRefreshToken(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("scope")
    val scope: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("expires_at")
    val expiresAt: Int = 0,
    @SerialName("expires_in")
    val expiresIn: Int = 3600,
)
