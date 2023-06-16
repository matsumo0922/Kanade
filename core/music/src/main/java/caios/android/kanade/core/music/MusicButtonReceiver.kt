package caios.android.kanade.core.music

import android.content.Context
import android.content.Intent
import android.support.v4.media.session.PlaybackStateCompat
import caios.android.kanade.core.model.player.PlayerEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicButtonReceiver : android.content.BroadcastReceiver() {

    @Inject
    lateinit var musicController: MusicController

    override fun onReceive(context: Context?, intent: Intent?) {
        val playerEvent = when(intent?.action?.toLongOrNull()) {
            PlaybackStateCompat.ACTION_PLAY -> PlayerEvent.Play
            PlaybackStateCompat.ACTION_PAUSE -> PlayerEvent.Pause
            PlaybackStateCompat.ACTION_STOP -> PlayerEvent.Stop
            PlaybackStateCompat.ACTION_SKIP_TO_NEXT -> PlayerEvent.SkipToNext
            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS -> PlayerEvent.SkipToPrevious
            else -> return
        }

        musicController.playerEvent(playerEvent)
    }
}