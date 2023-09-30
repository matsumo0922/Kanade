package caios.android.kanade.feature.information.message

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.NotificationConfigs
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class CloudMessagingService : FirebaseMessagingService() {

    private val manager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val notifyConfig = NotificationConfigs.firebase

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val intent = remoteMessage.toIntent()
        val messageTitle = intent.getStringExtra("gcm.notification.title")
        val messageBody = intent.getStringExtra("gcm.notification.body")

        Timber.d("onMessageReceived: Title: $messageTitle, Body: $messageBody")

        if (messageTitle != null && messageBody != null) {
            manager.notify(notifyConfig.notifyId, createMusicNotify(messageTitle, messageBody))
        }
    }

    override fun onNewToken(instanceToken: String) {
        Timber.d("onNewToken: Token=$instanceToken")
    }

    private fun createMusicNotify(notifyTitle: String, notifyText: String): Notification {
        val mainIntent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, pendingIntentFlags)

        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(this, notifyConfig.channelId).apply {
            setSmallIcon(R.drawable.vec_app_icon_no_background)
            setContentTitle(notifyTitle)
            setContentText(notifyText)
            setAutoCancel(false)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setContentIntent(pendingIntent)
        }

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (manager.getNotificationChannel(notifyConfig.channelId) != null) return

        val channelName = baseContext.getString(R.string.notify_channel_last_fm_name)
        val channelDescription = baseContext.getString(R.string.notify_channel_last_fm_description)

        val channel = NotificationChannel(
            notifyConfig.channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = channelDescription
        }

        manager.createNotificationChannel(channel)
    }
}
