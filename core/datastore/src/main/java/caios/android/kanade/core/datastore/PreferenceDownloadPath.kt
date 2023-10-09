package caios.android.kanade.core.datastore

import android.content.Context
import android.net.Uri
import androidx.core.content.edit
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceDownloadPath @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val preference by lazy { context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE) }

    fun saveUri(uri: Uri) {
        preference.edit {
            putString(KEY_PATH, uri.toString())
        }
    }

    fun getUri(): Uri? {
        return preference.getString(KEY_PATH, null)?.toUri()
    }

    companion object {
        private const val FILE_NAME = "DownloadPathPreference"
        private const val KEY_PATH = "path"
    }
}
