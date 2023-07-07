package caios.android.kanade.core.music

import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.view.LayoutInflater
import android.view.View
import androidx.compose.ui.graphics.toArgb
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat
import caios.android.kanade.core.design.databinding.LayoutDefaultArtworkBinding
import caios.android.kanade.core.design.theme.Blue40
import caios.android.kanade.core.design.theme.Green40
import caios.android.kanade.core.design.theme.Orange40
import caios.android.kanade.core.design.theme.Purple40
import caios.android.kanade.core.design.theme.Teal40
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.music.getMetadataBuilder
import caios.android.kanade.core.model.player.ControlAction
import caios.android.kanade.core.model.player.ControlKey
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.repository.MusicRepository
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MediaSessionManager(
    private val service: Service,
    private val player: ExoPlayer,
    private val mediaSession: MediaSessionCompat,
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
    private val queueManager: QueueManager,
    private val scope: CoroutineScope,
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
        Timber.d("onAudioFocusChange: type = $type")
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
            else -> {
                musicController.playerEvent(PlayerEvent.Pause)
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
                    val song = queueManager.getCurrentSong() ?: kotlin.run {
                        Timber.d("onCustomAction: cannot initialize because current song is null")
                        return
                    }

                    val playWhenReady = extras?.getBoolean(ControlKey.PLAY_WHEN_READY) ?: false
                    val progress = extras?.getLong(ControlKey.PROGRESS) ?: 0L

                    loadSong(song, playWhenReady, progress)
                }
                ControlAction.NEW_PLAY -> {
                    val song = queueManager.getCurrentSong() ?: return
                    val playWhenReady = extras?.getBoolean(ControlKey.PLAY_WHEN_READY) ?: false

                    if (playWhenReady) requestAudioFocus()
                    loadSong(song, playWhenReady)
                }
            }
        }
    }

    private fun loadSong(song: Song?, playWhenReady: Boolean, startPosition: Long = 0L) {
        song ?: return

        Timber.d("loadSong: ${song.title}, ${song.artist}, ${song.artwork}")

        scope.launch {
            val metadata = song.getMetadataBuilder().apply {
                putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, song.artwork.toBitmap(service))
            }

            mediaSession.setMetadata(metadata.build())

            if (playWhenReady) {
                musicRepository.addToPlayHistory(song)
            }
        }

        player.playWhenReady = playWhenReady
        player.setMediaItem(MediaItem.fromUri(song.uri), startPosition)
        player.prepare()
    }

    private fun withAudioFocus(f: () -> Unit) {
        if (requestAudioFocus()) {
            Timber.d("withAudioFocus: Granted")
            f()
        }
    }

    private fun requestAudioFocus(): Boolean {
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

    private suspend fun Artwork.toBitmap(context: Context): Bitmap? {
        val builder = when (this) {
            is Artwork.Internal -> {
                val char1 = name.elementAtOrNull(0)?.uppercase() ?: "?"
                val char2 = name.elementAtOrNull(1)?.uppercase() ?: char1

                val backgroundColor = when (name.toList().sumOf { it.code } % 5) {
                    0 -> Blue40
                    1 -> Green40
                    2 -> Orange40
                    3 -> Purple40
                    4 -> Teal40
                    else -> throw IllegalArgumentException("Unknown album name.")
                }

                val binding = LayoutDefaultArtworkBinding.inflate(LayoutInflater.from(context))

                binding.char1.text = char1
                binding.char2.text = char2
                binding.artworkLayout.setBackgroundColor(backgroundColor.toArgb())

                return binding.root.toBitmap()
            }
            is Artwork.Web -> ImageRequest.Builder(context).data(url)
            is Artwork.MediaStore -> ImageRequest.Builder(context).data(uri)
            else -> return null
        }.allowHardware(false)

        val request = builder.build()
        val result = (ImageLoader(context).execute(request) as? SuccessResult)?.drawable

        return (result as? BitmapDrawable)?.bitmap
    }

    private fun View.toBitmap(): Bitmap {
        measure(
            View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY),
        )

        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        layout(0, 0, measuredWidth, measuredHeight)
        draw(canvas)

        val scaleX = (0.7f * bitmap.width).toInt()
        val scaleY = (0.7f * bitmap.height).toInt()
        val startX = (bitmap.width - scaleX) / 2
        val startY = (bitmap.height - scaleY) / 2

        return Bitmap.createBitmap(bitmap, startX, startY, scaleX, scaleY, null, true)
    }
}
