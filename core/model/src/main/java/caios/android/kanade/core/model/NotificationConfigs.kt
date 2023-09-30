package caios.android.kanade.core.model

object NotificationConfigs {
    val music = NotificationConfig(
        serviceId = 1,
        notifyId = 1,
        channelId = "music",
    )
    val lastfm = NotificationConfig(
        serviceId = 2,
        notifyId = 2,
        channelId = "lastfm",
    )
    val firebase = NotificationConfig(
        serviceId = 3,
        notifyId = 3,
        channelId = "firebase",
    )
}

data class NotificationConfig(
    val serviceId: Int,
    val notifyId: Int,
    val channelId: String,
)
