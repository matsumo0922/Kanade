package caios.android.kanade.core.datastore

import android.content.Context
import caios.android.kanade.core.model.Version
import caios.android.kanade.core.model.entity.VersionEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject

class PreferenceVersion @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private var data: List<Version>? = null

    fun get(): List<Version> {
        data?.let { return it }

        val serializer = ListSerializer(VersionEntity.serializer())
        val json = context.resources.openRawResource(R.raw.versions).bufferedReader().use { it.readText() }

        return Json.decodeFromString(serializer, json)
            .map {
                Version(
                    name = it.versionName,
                    code = it.versionCode,
                    date = it.date,
                    message = if (Locale.getDefault() == Locale.JAPAN) it.logJp else it.logEn,
                )
            }
            .also {
                data = it
            }
    }
}
