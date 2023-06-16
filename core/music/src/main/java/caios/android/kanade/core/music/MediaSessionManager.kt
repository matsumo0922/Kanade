package caios.android.kanade.core.music

import android.app.Service
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.music.toMetadata
import caios.android.kanade.core.model.player.ControlAction
import caios.android.kanade.core.model.player.ControlKey
import caios.android.kanade.core.model.player.PlayerEvent
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import timber.log.Timber

class MediaSessionManager(
    private val service: Service,
    private val player: ExoPlayer,
    private val mediaSession: MediaSessionCompat,
    private val musicController: MusicController,
    private val queueManager: QueueManager,
) {
    private val audioManager by lazy { service.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private val audioFocusRequest by lazy {
        AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN).run {
            setAudioAttributes(
                AudioAttributesCompat.Builder().run {
                    setUsage(AudioAttributesCompat.USAGE_MEDIA)
                    setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
                    build()
                },
            )
            setOnAudioFocusChangeListener(audioFocusChangeListener, Handler(Looper.myLooper()!!))
            setWillPauseWhenDucked(true)
            build()
        }
    }

    private val audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { type ->
        when (type) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                musicController.playerEvent(PlayerEvent.Pause)
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                musicController.playerEvent(PlayerEvent.PauseTransient)
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                musicController.playerEvent(PlayerEvent.Dack(true))
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                musicController.playerEvent(PlayerEvent.Dack(false))
                musicController.playerEvent(PlayerEvent.Play)
            }
        }
    }

    val callback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            if (!musicController.isInitialized.value) {
                Timber.d("onPlay: cannot play because MusicController is not initialized")

                musicController.playerEvent(PlayerEvent.Initialize(true))
                return
            }

            withAudioFocus {
                player.play()
            }
        }

        override fun onPause() {
            releaseAudioFocus()
            player.pause()
        }

        override fun onStop() {
            releaseAudioFocus()
            player.stop()
        }

        override fun onSkipToNext() {
            if (!player.playWhenReady || (player.playWhenReady && releaseAudioFocus())) {
                loadSong(queueManager.skipToNext(), player.playWhenReady)
            }
        }

        override fun onSkipToPrevious() {
            if (!player.playWhenReady || (player.playWhenReady && releaseAudioFocus())) {
                if (player.currentPosition <= 5000) {
                    loadSong(queueManager.skipToPrevious(), player.playWhenReady)
                } else {
                    onSeekTo(0L)
                }
            }
        }

        override fun onSeekTo(pos: Long) {
            player.seekTo(pos)
        }

        override fun onCustomAction(action: String?, extras: Bundle?) {
            Timber.d("onCustomAction: $action")

            when (action) {
                ControlAction.INITIALIZE -> {
                    withAudioFocus {
                        val song = queueManager.getCurrentSong() ?: kotlin.run {
                            Timber.d("onCustomAction: cannot initialize because current song is null")
                            return@withAudioFocus
                        }

                        val playWhenReady = extras?.getBoolean(ControlKey.PLAY_WHEN_READY) ?: false
                        val progress = extras?.getLong(ControlKey.PROGRESS) ?: 0L

                        loadSong(song, playWhenReady, progress)
                    }
                }
                ControlAction.NEW_PLAY -> {
                    withAudioFocus {
                        val song = queueManager.getCurrentSong() ?: return@withAudioFocus
                        val playWhenReady = extras?.getBoolean(ControlKey.PLAY_WHEN_READY) ?: false

                        loadSong(song, playWhenReady)
                    }
                }
            }
        }
    }

    private fun loadSong(song: Song, playWhenReady: Boolean, startPosition: Long = 0L) {
        Timber.d("loadSong: ${song.title}, ${song.artist}, ${song.artwork}")

        mediaSession.setMetadata(song.toMetadata())

        player.playWhenReady = playWhenReady
        player.setMediaItem(MediaItem.fromUri(song.uri), startPosition)
        player.prepare()
    }

    private fun withAudioFocus(f: () -> Unit) {
        if (requestAudioForcus()) {
            f()
        }
    }

    private fun requestAudioForcus(): Boolean {
        when (AudioManagerCompat.requestAudioFocus(audioManager, audioFocusRequest)) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> return true
            AudioManager.AUDIOFOCUS_REQUEST_FAILED -> Timber.d("Audio focus request failed")
            AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> Timber.d("Audio focus request delayed")
            else -> Timber.d("Audio focus request unknown")
        }

        return false
    }

    private fun releaseAudioFocus(): Boolean {
        return (AudioManagerCompat.abandonAudioFocusRequest(audioManager, audioFocusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
    }
}
