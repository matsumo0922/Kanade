package caios.android.kanade.core.datastore

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceToken @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val preference by lazy { context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE) }

    fun set(key: String, token: String) {
        preference.edit {
            putString(key, token)
        }
    }

    fun get(key: String): String? {
        return preference.getString(key, null)
    }

    companion object {
        const val KEY_MUSIXMATCH = "musixmatch"

        private const val PREFERENCE = "token"
    }
}
