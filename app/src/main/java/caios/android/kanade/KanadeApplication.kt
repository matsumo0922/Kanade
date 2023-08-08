package caios.android.kanade

import android.app.Application
import caios.android.kanade.core.common.network.KanadeDebugTree
import com.google.android.material.color.DynamicColors
import com.yausername.aria2c.Aria2c
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class KanadeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(KanadeDebugTree())

        DynamicColors.applyToActivitiesIfAvailable(this)

        YoutubeDL.init(applicationContext)
        FFmpeg.init(applicationContext)
        Aria2c.init(applicationContext)
    }
}
