package caios.android.kanade.core.repository

import caios.android.kanade.core.datastore.PreferenceYTMusic
import caios.android.kanade.core.model.entity.YTMusicInfo
import caios.android.kanade.core.model.entity.YTMusicOAuthCode
import caios.android.kanade.core.model.entity.YTMusicOAuthRefreshToken
import caios.android.kanade.core.model.entity.YTMusicOAuthToken
import caios.android.kanade.core.repository.util.parse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.Parameters
import javax.inject.Inject

interface YTMusicRepository {

    suspend fun getOAuthCode(): YTMusicOAuthCode?
    suspend fun getOAuthToken(code: YTMusicOAuthCode): YTMusicOAuthToken?

    suspend fun refreshToken(token: YTMusicOAuthToken): YTMusicOAuthRefreshToken?

    fun saveToken(token: YTMusicOAuthToken)
    fun removeToken()
    fun getTokenFilePath(): String
    fun getOAuthToken(): YTMusicOAuthToken?
}

class YTMusicRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val preferenceYTMusic: PreferenceYTMusic,
) : YTMusicRepository {

    init {
        client.config {
            defaultRequest {
                header("User-Agent", YTMusicInfo.OAUTH_USER_AGENT)
            }
        }
    }

    override suspend fun getOAuthCode(): YTMusicOAuthCode? {
        return client.submitForm(
            url = YTMusicInfo.OAUTH_CODE_URL,
            formParameters = Parameters.build {
                append("client_id", YTMusicInfo.OAUTH_CLIENT_ID)
                append("scope", YTMusicInfo.OAUTH_SCOPE)
            },
        ).parse()
    }

    override suspend fun getOAuthToken(code: YTMusicOAuthCode): YTMusicOAuthToken? {
        return client.submitForm(
            url = YTMusicInfo.OAUTH_TOKEN_URL,
            formParameters = Parameters.build {
                append("client_id", YTMusicInfo.OAUTH_CLIENT_ID)
                append("client_secret", YTMusicInfo.OAUTH_CLIENT_SECRET)
                append("code", code.deviceCode)
                append("grant_type", "http://oauth.net/grant_type/device/1.0")
            },
        ).parse<YTMusicOAuthToken>()?.let {
            it.copy(expiresAt = (System.currentTimeMillis() / 1000).toInt() + it.expiresIn)
        }
    }

    override suspend fun refreshToken(token: YTMusicOAuthToken): YTMusicOAuthRefreshToken? {
        return client.submitForm(
            url = YTMusicInfo.OAUTH_TOKEN_URL,
            formParameters = Parameters.build {
                append("client_id", YTMusicInfo.OAUTH_CLIENT_ID)
                append("client_secret", YTMusicInfo.OAUTH_CLIENT_SECRET)
                append("refresh_token", token.refreshToken)
                append("grant_type", "refresh_token")
            },
        ).parse<YTMusicOAuthRefreshToken>()?.let {
            it.copy(expiresAt = (System.currentTimeMillis() / 1000).toInt() + it.expiresIn)
        }
    }

    override fun saveToken(token: YTMusicOAuthToken) {
        preferenceYTMusic.saveToken(token)
    }

    override fun removeToken() {
        preferenceYTMusic.removeToken()
    }

    override fun getTokenFilePath(): String {
        return preferenceYTMusic.getTokenFilePath()
    }

    override fun getOAuthToken(): YTMusicOAuthToken? {
        return preferenceYTMusic.getToken()
    }
}
