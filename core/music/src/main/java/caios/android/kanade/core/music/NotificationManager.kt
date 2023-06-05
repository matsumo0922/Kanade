package caios.android.kanade.core.music

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import caios.android.kanade.core.design.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val player: ExoPlayer,
) {

    private val manager = NotificationManagerCompat.from(context)

    @UnstableApi
    private fun bindNotification(mediaSession: MediaSession) {
        PlayerNotificationManager.Builder(context, NOTIFY_ID, NOTIFY_CHANNEL_ID)
            .setMediaDescriptionAdapter(
                NotificationAdapter(
                    context = context,
                    pendingIntent = mediaSession.sessionActivity
                )
            )
            .setSmallIconResourceId(R.drawable.vec_songs_off)
            .build()
            .also {
                it.setMediaSessionToken(mediaSession.sessionCompatToken)
                it.setUseFastForwardActionInCompactView(true)
                it.setUseRewindActionInCompactView(true)
                it.setUseNextActionInCompactView(false)
                it.setPriority(NotificationCompat.PRIORITY_LOW)
                it.setPlayer(player)
            }
    }

    private fun createNotificationChannel() {
        if (manager.getNotificationChannel(NOTIFY_CHANNEL_ID) != null) return

        val channelName = context.getString(R.string.notify_channel_music_name)
        val channelDescription = context.getString(R.string.notify_channel_music_description)

        val channel = NotificationChannel(
            NOTIFY_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = channelDescription
        }

        manager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFY_ID = 92
        const val NOTIFY_CHANNEL_ID = "KanadeNotify1"
    }
}