package caios.android.kanade.core.music

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.design.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(KanadeDispatcher.IO) private val dispatcher: CoroutineDispatcher,
    private val player: ExoPlayer,
) {

    private val manager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    @UnstableApi
    fun startNotificationService(
        mediaSessionService: MediaSessionService,
        mediaSession: MediaSession,
    ) {
        bindNotification(mediaSession)
        startForegroundNotification(mediaSessionService)
    }

    @UnstableApi
    private fun bindNotification(mediaSession: MediaSession) {
        PlayerNotificationManager.Builder(context, NOTIFY_ID, NOTIFY_CHANNEL_ID)
            .setMediaDescriptionAdapter(
                NotificationAdapter(
                    context = context,
                    pendingIntent = mediaSession.sessionActivity,
                    scope = CoroutineScope(dispatcher),
                ),
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

    private fun startForegroundNotification(mediaSessionService: MediaSessionService) {
        val notification = NotificationCompat.Builder(context, NOTIFY_CHANNEL_ID)
            .setSmallIcon(R.drawable.vec_songs_off)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        mediaSessionService.startForeground(NOTIFY_ID, notification)
    }

    companion object {
        const val NOTIFY_ID = 92
        const val NOTIFY_CHANNEL_ID = "KanadeNotify1"
    }
}
