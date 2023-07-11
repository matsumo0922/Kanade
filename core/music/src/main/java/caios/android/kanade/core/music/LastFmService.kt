package caios.android.kanade.core.music

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.design.R
import caios.android.kanade.core.repository.LastFmRepository
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class LastFmService : Service(), CoroutineScope {

    private val manager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private var processJob: Job? = null
    private val scrapingInterval = if (BuildConfig.DEBUG) 500L else 1000L * 3

    @Inject
    @Dispatcher(KanadeDispatcher.IO)
    lateinit var dispatcher: CoroutineDispatcher

    @Inject
    lateinit var musicRepository: MusicRepository

    @Inject
    lateinit var lastFmRepository: LastFmRepository

    override val coroutineContext: CoroutineContext
        get() = dispatcher + SupervisorJob()

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (processJob == null) {
            createNotificationChannel()

            processJob = createJob()
            processJob?.start()
        } else {
            Timber.d("LastFmService is already running.")
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        processJob?.cancel()
        processJob = null
    }

    private fun createJob() = launch(start = CoroutineStart.LAZY) {
        lastFmRepository.fetchArtistDetails()
        lastFmRepository.fetchAlbumDetails()

        val artists = musicRepository.artists.toList()
        val albums = musicRepository.albums.toList()

        val ignores = lastFmRepository.getIgnores()
        val notDownloadedArtists = artists.filterNot { lastFmRepository.getArtistDetails()[it.artistId] != null || ignores.artistIds.contains(it.artistId) }
        val notDownloadedAlbums = albums.filterNot { lastFmRepository.getAlbumDetails()[it.albumId] != null || ignores.albumIds.contains(it.albumId) }

        if (notDownloadedArtists.isEmpty() && notDownloadedAlbums.isEmpty()) {
            Timber.d("All artists and albums are downloaded.")
            return@launch
        }

        var downloadedCount = 1
        val totalCount = notDownloadedArtists.size + notDownloadedAlbums.size

        for (artist in notDownloadedArtists) {
            Timber.d("Downloading artist: ${artist.artist}")

            setForegroundService(true, artist.artist, downloadedCount, totalCount)

            if (lastFmRepository.artistDetail(artist) == null) {
                lastFmRepository.setIgnoreArtist(artist)
                Timber.d("Artist is not found: ${artist.artist}")
            }

            downloadedCount++
            delay(scrapingInterval)
        }

        for (album in notDownloadedAlbums) {
            Timber.d("Downloading album: ${album.album}")

            setForegroundService(true, album.album, downloadedCount, totalCount)

            if (lastFmRepository.albumDetail(album) == null) {
                lastFmRepository.setIgnoreAlbum(album)
                Timber.d("Album is not found: ${album.album}")
            }

            downloadedCount++
            delay(scrapingInterval)
        }

        musicRepository.fetchAlbumArtwork()
        musicRepository.fetchArtistArtwork()

        setForegroundService(false, "", 0, 0)
    }

    private fun setForegroundService(isForeground: Boolean, title: String, progress: Int, max: Int) {
        if (isForeground) {
            startForeground(NOTIFY_ID, createMusicNotification(baseContext, title, progress, max))
        } else {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
    }

    private fun createMusicNotification(context: Context, songTitle: String, progress: Int, max: Int): Notification {
        val mainIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, NOTIFY_CHANNEL_ID)
            .setSmallIcon(R.drawable.vec_songs_off)
            .setContentTitle(getString(R.string.notify_last_fm_title))
            .setContentText(songTitle)
            .setSubText("%.1f%%".format((progress / max.toDouble()) * 100))
            .setAutoCancel(false)
            .setColorized(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(mainPendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (manager.getNotificationChannel(NOTIFY_CHANNEL_ID) != null) return

        val channelName = baseContext.getString(R.string.notify_channel_last_fm_name)
        val channelDescription = baseContext.getString(R.string.notify_channel_last_fm_description)

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
        private const val NOTIFY_ID = 94
        private const val NOTIFY_INTENT_ID = 95
        private const val NOTIFY_CHANNEL_ID = "KanadeNotify2"
    }
}
