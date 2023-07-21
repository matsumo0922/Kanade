package caios.android.kanade.core.music

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerState
import kotlinx.coroutines.flow.first
import timber.log.Timber

class NotificationManager(
    private val service: Service,
    private val mediaSession: MediaSessionCompat,
    private val musicController: MusicController,
) {

    private val manager = NotificationManagerCompat.from(service.baseContext)
    private var isForeground = false

    init {
        createNotificationChannel()
    }

    @SuppressLint("MissingPermission")
    suspend fun setForegroundService(isForeground: Boolean) {
        Timber.d("setForegroundService: $isForeground, ${this.isForeground}")

        val notification = createMusicNotification(
            context = service.baseContext,
            song = musicController.currentSong.first(),
        )

        try {
            if (isForeground) {
                if (!this.isForeground) {
                    service.startForeground(NOTIFY_ID, notification)
                }
            } else {
                manager.notify(NOTIFY_ID, notification)
                service.stopForeground(false)
            }
        } catch (e: Throwable) {
            Timber.e(e, "cannot set foreground service.")
        }

        this.isForeground = isForeground
    }

    @SuppressLint("WrongConstant")
    private suspend fun createMusicNotification(context: Context, song: Song?): Notification {
        val mainIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("notify", true)
        }
        val mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val isPlaying = (musicController.playerState.first() == PlayerState.Playing)
        val pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val stopActionIntent = Intent(PlaybackStateCompat.STATE_BUFFERING.toString()).apply { addFlags(0x01000000) }
        val stopActionPendingIntent = PendingIntent.getBroadcast(context, NOTIFY_INTENT_ID, stopActionIntent, pendingIntentFlags)

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFY_CHANNEL_ID)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(stopActionPendingIntent),
            )
            .setSmallIcon(R.drawable.vec_songs_off)
            .setContentTitle(song?.title)
            .setContentText(song?.artist)
            .setAutoCancel(false)
            .setColorized(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(mainPendingIntent)

        val playActionIntent = Intent(PlaybackStateCompat.ACTION_PLAY.toString()).apply { addFlags(0x01000000) }
        val playActionPendingIntent = PendingIntent.getBroadcast(context, NOTIFY_INTENT_ID, playActionIntent, pendingIntentFlags)
        val playAction = NotificationCompat.Action(R.drawable.vec_play, null, playActionPendingIntent)

        val pauseActionIntent = Intent(PlaybackStateCompat.ACTION_PAUSE.toString()).apply { addFlags(0x01000000) }
        val pauseActionPendingIntent = PendingIntent.getBroadcast(context, NOTIFY_INTENT_ID, pauseActionIntent, pendingIntentFlags)
        val pauseAction = NotificationCompat.Action(R.drawable.vec_pause, null, pauseActionPendingIntent)

        val skipToNextActionIntent = Intent(PlaybackStateCompat.ACTION_SKIP_TO_NEXT.toString()).apply { addFlags(0x01000000) }
        val skipToNextActionPendingIntent = PendingIntent.getBroadcast(context, NOTIFY_INTENT_ID, skipToNextActionIntent, pendingIntentFlags)
        val skipToNextAction = NotificationCompat.Action(R.drawable.vec_skip_to_next, null, skipToNextActionPendingIntent)

        val skipToPreviousActionIntent = Intent(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS.toString()).apply { addFlags(0x01000000) }
        val skipToPreviousActionPendingIntent = PendingIntent.getBroadcast(context, NOTIFY_INTENT_ID, skipToPreviousActionIntent, pendingIntentFlags)
        val skipToPreviousAction = NotificationCompat.Action(R.drawable.vec_skip_to_previous, null, skipToPreviousActionPendingIntent)

        notificationBuilder.addAction(skipToPreviousAction)
        notificationBuilder.addAction(if (isPlaying) pauseAction else playAction)
        notificationBuilder.addAction(skipToNextAction)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (manager.getNotificationChannel(NOTIFY_CHANNEL_ID) != null) return

        val channelName = service.baseContext.getString(R.string.notify_channel_music_name)
        val channelDescription = service.baseContext.getString(R.string.notify_channel_music_description)

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
        const val NOTIFY_INTENT_ID = 93
        const val NOTIFY_CHANNEL_ID = "KanadeNotify1"
    }
}
