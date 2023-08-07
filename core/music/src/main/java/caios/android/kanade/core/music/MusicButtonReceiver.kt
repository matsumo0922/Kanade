package caios.android.kanade.core.music

import android.content.Context
import android.content.Intent
import caios.android.kanade.core.model.player.PlayerEvent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MusicButtonReceiver : android.content.BroadcastReceiver() {

    @Inject
    lateinit var musicController: MusicController

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive: ${intent?.action?.toLongOrNull()}")

        val playerEvent = when (intent?.action) {
            NotificationManager.ACTION_PLAY -> PlayerEvent.Play
            NotificationManager.ACTION_PAUSE -> PlayerEvent.Pause
            NotificationManager.ACTION_STOP -> PlayerEvent.Stop
            NotificationManager.ACTION_SKIP_TO_NEXT -> PlayerEvent.SkipToNext
            NotificationManager.ACTION_SKIP_TO_PREVIOUS -> PlayerEvent.SkipToPrevious
            else -> return
        }

        musicController.playerEvent(playerEvent)
    }
}
