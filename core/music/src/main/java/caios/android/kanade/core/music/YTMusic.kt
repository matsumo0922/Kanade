package caios.android.kanade.core.music

import android.content.Context
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.common.network.util.suspendRunCatching
import caios.android.kanade.core.model.entity.YTMusicOAuthCode
import caios.android.kanade.core.model.entity.YTMusicOAuthRefreshToken
import caios.android.kanade.core.model.entity.YTMusicOAuthToken
import caios.android.kanade.core.model.entity.YTMusicSearch
import caios.android.kanade.core.repository.YTMusicRepository
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

interface YTMusic {
    fun isInitialized(): Boolean

    suspend fun getOAuthCode(): Result<YTMusicOAuthCode>
    suspend fun getOAuthToken(code: YTMusicOAuthCode): Result<YTMusicOAuthToken>

    suspend fun removeToken()
    suspend fun refreshToken(token: YTMusicOAuthToken): Result<YTMusicOAuthRefreshToken>
    suspend fun search(query: String, filters: Filters? = null, scopes: Scopes? = null): Result<List<YTMusicSearch>>

    enum class Language(val value: String) {
        ENGLISH("en"),
        SPANISH("es"),
        FRENCH("fr"),
        ITALIAN("it"),
        JAPANESE("ja"),
        KOREAN("ko"),
        PORTUGUESE("pt"),
        RUSSIAN("ru"),
        TURKISH("tr"),
        CHINESE_CHINA("zh_CN"),
        CHINESE_TAIWAN("zh_TW"),
    }

    enum class Filters(val value: String) {
        ALBUMS("albums"),
        ARTISTS("artists"),
        PLAYLISTS("playlists"),
        COMMUNITY_PLAYLISTS("community_playlists"),
        FEATURED_PLAYLIST("featured_playlists"),
        SONGS("songs"),
        VIDEOS("videos"),
        PROFILES("profiles"),
    }

    enum class Scopes(val value: String) {
        LIBRARY("library"),
        UPLOADS("uploads"),
    }
}

class YTMusicImpl @Inject constructor(
    private val ytMusicRepository: YTMusicRepository,
    @ApplicationContext private val context: Context,
    @Dispatcher(KanadeDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : YTMusic {

    @OptIn(ExperimentalSerializationApi::class)
    private val formatter = Json {
        isLenient = true
        prettyPrint = true
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        explicitNulls = false
    }

    override fun isInitialized(): Boolean {
        return ytMusicRepository.getOAuthToken() != null
    }

    override suspend fun getOAuthCode(): Result<YTMusicOAuthCode> = withContext(ioDispatcher) {
        suspendRunCatching {
            ytMusicRepository.getOAuthCode()!!
        }
    }

    override suspend fun getOAuthToken(code: YTMusicOAuthCode): Result<YTMusicOAuthToken> = withContext(ioDispatcher) {
        suspendRunCatching {
            ytMusicRepository.getOAuthToken(code)!!.also {
                ytMusicRepository.saveToken(it)
            }
        }
    }

    override suspend fun removeToken() {
        ytMusicRepository.removeToken()
    }

    override suspend fun refreshToken(token: YTMusicOAuthToken): Result<YTMusicOAuthRefreshToken> = withContext(ioDispatcher) {
        suspendRunCatching {
            ytMusicRepository.refreshToken(token)!!
        }
    }

    override suspend fun search(query: String, filters: YTMusic.Filters?, scopes: YTMusic.Scopes?): Result<List<YTMusicSearch>> = withContext(ioDispatcher) {
        suspendRunCatching {
            launchPythonScript {
                val serializer = ListSerializer(YTMusicSearch.serializer())
                val result = it.callAttr("search", query, filters?.value, scopes?.value).toString()
                    .replaceSingleQuotesWithDoubleQuotes()
                    .replace("None", "null")

                return@launchPythonScript formatter.decodeFromString(serializer, result)
            }!!
        }
    }

    private suspend fun <T> launchPythonScript(action: (PyObject) -> T): T? {
        return try {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(context))
            }

            checkTokenExpired()

            val python = Python.getInstance()
            val module = python.getModule("ytmusic")

            action.invoke(module.callAttr("YTMusicClient", ytMusicRepository.getTokenFilePath()))
        } catch (e: Throwable) {
            Timber.w(e)
            null
        }
    }

    private suspend fun checkTokenExpired() {
        val token = ytMusicRepository.getOAuthToken() ?: error("Token is not saved.")

        if (token.expiresAt < (System.currentTimeMillis() / 1000)) {
            val refreshToken = ytMusicRepository.refreshToken(token)!!
            val newToken = token.copy(
                accessToken = refreshToken.accessToken,
                expiresAt = refreshToken.expiresAt,
                expiresIn = refreshToken.expiresIn,
            )

            ytMusicRepository.saveToken(newToken)
        }
    }

    private fun String.replaceSingleQuotesWithDoubleQuotes(): String {
        return "'(.*?)'".toRegex().replace(this) { "\"${it.groupValues[1]}\"" }
    }
}
