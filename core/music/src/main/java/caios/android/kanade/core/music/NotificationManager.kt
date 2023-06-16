package caios.android.kanade.core.music

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import caios.android.kanade.core.design.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class NotificationManager(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) {

    private val manager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    @SuppressLint("MissingPermission")
    fun setForegroundService(service: Service, isForeground: Boolean) {
        val notification = NotificationCompat.Builder(context, NOTIFY_CHANNEL_ID)
            .setSmallIcon(R.drawable.vec_songs_off)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        if (isForeground) {
            service.startForeground(NOTIFY_ID, notification)
        } else {
            manager.notify(NOTIFY_ID, notification)
            service.stopForeground(Service.STOP_FOREGROUND_DETACH)
        }
    }

    fun bindNotification(mediaSession: MediaSessionCompat, player: Player) {
        PlayerNotificationManager.Builder(context, NOTIFY_ID, NOTIFY_CHANNEL_ID)
            .setMediaDescriptionAdapter(
                NotificationAdapter(
                    context = context,
                    pendingIntent = null,
                    scope = CoroutineScope(dispatcher),
                ),
            )
            .setSmallIconResourceId(R.drawable.vec_songs_off)
            .build()
            .also {
                it.setMediaSessionToken(mediaSession.sessionToken)
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
