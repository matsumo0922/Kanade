package caios.android.kanade.core.music

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.model.music.toMediaItem
import caios.android.kanade.core.repository.MusicRepository
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat(){

    @Inject
    lateinit var musicRepository: MusicRepository

    @Inject
    lateinit var musicController: MusicController

    @Inject
    lateinit var queueManager: QueueManager

    @Inject
    @Dispatcher(KanadeDispatcher.IO)
    lateinit var io: CoroutineDispatcher

    @Inject
    @Dispatcher(KanadeDispatcher.Main)
    lateinit var main: CoroutineDispatcher

    private val supervisorJob = SupervisorJob()
    private val scope by lazy { CoroutineScope(io + supervisorJob) }

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionManager: MediaSessionManager
    private lateinit var notificationManager: NotificationManager

    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(baseContext).build().apply {
            setHandleAudioBecomingNoisy(true)
            addListener(playerEventListener)
        }
    }

    private val playerEventListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            musicController.setPlayerPlaying(isPlaying)
            notificationManager.setForegroundService(this@MusicService, isPlaying)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            musicController.setPlayerState(playbackState)
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaItem?.mediaMetadata?.let { musicController.setPlayerItem(it) }
        }
    }

    private val updateProcess by lazy {
        scope.launch(start = CoroutineStart.LAZY, context = main) {
            while (isActive) {
                if (exoPlayer.playWhenReady) {
                    musicController.setPlayerPosition(exoPlayer.currentPosition)
                }

                delay(200)
            }
        }
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot {
        return BrowserRoot(MEDIA_BROWSER_ROOT_ID, null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        scope.launch {
            musicRepository.config.first()
            musicRepository.fetchSongs()

            val songs = musicRepository.songs
            val items = songs.map { it.toMediaItem() }

            result.sendResult(items.toMutableList())
        }

        result.detach()
    }

    override fun onCreate() {
        super.onCreate()

        notificationManager = NotificationManager(baseContext, io)

        mediaSession = MediaSessionCompat(this, "KanadeMediaSession").apply {
            setSessionActivity(null)
            setFlags(MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS)
            this@MusicService.sessionToken = sessionToken
        }

        mediaSessionManager = MediaSessionManager(
            service = this,
            player = exoPlayer,
            mediaSession = mediaSession,
            musicController = musicController,
            queueManager = queueManager,
        )

        mediaSession.setCallback(mediaSessionManager.callback)
        notificationManager.bindNotification(mediaSession, exoPlayer)

        updateProcess.start()

        Timber.d("MusicService onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (::mediaSession.isInitialized) MediaButtonReceiver.handleIntent(mediaSession, intent)
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        exoPlayer.stop()
        exoPlayer.release()
    }

    companion object {
        private const val MEDIA_BROWSER_ROOT_ID = "kanade-media-root2"
    }
}
