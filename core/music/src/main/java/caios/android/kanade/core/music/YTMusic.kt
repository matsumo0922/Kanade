package caios.android.kanade.core.music

import android.content.Context
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.common.network.util.suspendRunCatching
import caios.android.kanade.core.model.entity.YTMusicOAuthCode
import caios.android.kanade.core.model.entity.YTMusicOAuthRefreshToken
import caios.android.kanade.core.model.entity.YTMusicOAuthToken
import caios.android.kanade.core.repository.YTMusicRepository
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.logging.Filter
import javax.inject.Inject

interface YTMusic {
    suspend fun getOAuthCode(): Result<YTMusicOAuthCode>
    suspend fun getOAuthToken(code: YTMusicOAuthCode): Result<YTMusicOAuthToken>

    suspend fun refreshToken(token: YTMusicOAuthToken): Result<YTMusicOAuthRefreshToken>
    suspend fun search(query: String, filter: Filter, scopes: Scopes): Result<String>

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

    override suspend fun getOAuthCode(): Result<YTMusicOAuthCode> = withContext(ioDispatcher) {
        suspendRunCatching {
            ytMusicRepository.getOAuthCode()!!
        }
    }

    override suspend fun getOAuthToken(code: YTMusicOAuthCode): Result<YTMusicOAuthToken> = withContext(ioDispatcher) {
        suspendRunCatching {
            ytMusicRepository.getOAuthToken(code)!!
        }
    }

    override suspend fun refreshToken(token: YTMusicOAuthToken): Result<YTMusicOAuthRefreshToken> = withContext(ioDispatcher) {
        suspendRunCatching {
            ytMusicRepository.refreshToken(token)!!
        }
    }

    override suspend fun search(query: String, filter: Filter, scopes: YTMusic.Scopes): Result<String> {
        launchPythonScript("ytmusic") {
            it.callAttr("")
        }
    }

    private fun launchPythonScript(module: String, action: (PyObject) -> Unit) {
        try {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(context))
            }

            val python = Python.getInstance()
            val module = python.getModule(module)

            action.invoke(module)
        } catch (e: Throwable) {
            Timber.w(e)
        }
    }
}
