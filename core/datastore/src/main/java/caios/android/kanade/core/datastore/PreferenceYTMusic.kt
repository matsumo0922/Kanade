package caios.android.kanade.core.datastore

import android.content.Context
import caios.android.kanade.core.model.entity.YTMusicOAuthToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

class PreferenceYTMusic @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val tokenFile get() = File(context.filesDir, FILE_NAME)

    fun saveToken(token: YTMusicOAuthToken) {
        Json.encodeToString(YTMusicOAuthToken.serializer(), token).also {
            tokenFile.writeText(it)
        }
    }

    fun removeToken() {
        tokenFile.delete()
    }

    fun getToken(): YTMusicOAuthToken? {
        if (!tokenFile.exists()) return null

        return tokenFile.readText().let {
            Json.decodeFromString(YTMusicOAuthToken.serializer(), it)
        }
    }

    fun getTokenFilePath(): String {
        return tokenFile.absolutePath
    }

    companion object {
        private const val FILE_NAME = "YTMusicToken.json"
    }
}
