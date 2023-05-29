package caios.android.kanade

import android.app.Application
import caios.android.kanade.core.common.network.KanadeDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class KanadeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(KanadeDebugTree())
    }
}
