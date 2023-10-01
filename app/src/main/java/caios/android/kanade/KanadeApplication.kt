package caios.android.kanade

import android.app.Application
import android.content.Intent
import android.os.Build
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.common.network.KanadeDebugTree
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.feature.report.CrushReportActivity
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class KanadeApplication : Application() {

    @Inject
    lateinit var kanadeConfig: KanadeConfig

    @Inject
    lateinit var widgetUpdater: WidgetUpdater

    @Inject
    @Dispatcher(KanadeDispatcher.IO)
    lateinit var io: CoroutineDispatcher

    override fun onCreate() {
        super.onCreate()

        Timber.plant(KanadeDebugTree())

        DynamicColors.applyToActivitiesIfAvailable(this)

        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            startCrushReportActivity(e)
        }
    }

    private fun startCrushReportActivity(e: Throwable) {
        Timber.e(e)

        startActivity(
            Intent(this, CrushReportActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("report", getVersionReport() + "\n" + e.stackTraceToString())
            },
        )
    }

    private fun getVersionReport() = buildString {
        val release = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Build.VERSION.RELEASE_OR_CODENAME else Build.VERSION.RELEASE

        appendLine("Version: ${kanadeConfig.versionName} (${kanadeConfig.versionCode})")
        appendLine("Device Information: $release (${Build.VERSION.SDK_INT})")
        appendLine("Device Model: ${Build.MODEL} (${Build.MANUFACTURER})")
        appendLine("Supported ABIs: ${Build.SUPPORTED_ABIS.contentToString()}")
    }
}
