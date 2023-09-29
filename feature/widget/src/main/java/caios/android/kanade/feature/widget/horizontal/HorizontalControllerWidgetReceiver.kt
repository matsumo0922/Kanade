package caios.android.kanade.feature.widget.horizontal

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.databinding.LayoutDefaultArtworkBinding
import caios.android.kanade.core.design.theme.Blue40
import caios.android.kanade.core.design.theme.Green40
import caios.android.kanade.core.design.theme.Orange40
import caios.android.kanade.core.design.theme.Purple40
import caios.android.kanade.core.design.theme.Teal40
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.music.NotificationManager
import caios.android.kanade.feature.widget.ControllerWidgetContract.Args.KEY_CURRENT_SONG_ARTIST
import caios.android.kanade.feature.widget.ControllerWidgetContract.Args.KEY_CURRENT_SONG_ARTWORK
import caios.android.kanade.feature.widget.ControllerWidgetContract.Args.KEY_CURRENT_SONG_ID
import caios.android.kanade.feature.widget.ControllerWidgetContract.Args.KEY_CURRENT_SONG_TITLE
import caios.android.kanade.feature.widget.ControllerWidgetContract.Args.KEY_IS_PLAYING
import caios.android.kanade.feature.widget.ControllerWidgetContract.Args.KEY_IS_PLUS_USER
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class HorizontalControllerWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var musicController: MusicController

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var latestIsPlaying = false
    private var latestIsPlusUser = false

    override val glanceAppWidget: GlanceAppWidget
        get() = HorizontalControllerWidget()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        updateWidgets(
            context = context,
            isPlaying = latestIsPlaying,
            isPlusUser = latestIsPlusUser,
        )
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_REQUEST_UPDATE -> {
                updateWidgets(
                    context = context,
                    isPlaying = intent.getBooleanExtra(KEY_IS_PLAYING, false),
                    isPlusUser = intent.getBooleanExtra(KEY_IS_PLUS_USER, false),
                )
            }
            NotificationManager.ACTION_PLAY -> musicController.playerEvent(PlayerEvent.Play)
            NotificationManager.ACTION_PAUSE -> musicController.playerEvent(PlayerEvent.Pause)
            NotificationManager.ACTION_SKIP_TO_NEXT -> musicController.playerEvent(PlayerEvent.SkipToNext)
            NotificationManager.ACTION_SKIP_TO_PREVIOUS -> musicController.playerEvent(PlayerEvent.SkipToPrevious)
        }
    }

    private fun updateWidgets(
        context: Context,
        isPlaying: Boolean,
        isPlusUser: Boolean,
    ) {
        scope.launch {
            val currentSong = musicController.currentSong.value
            val artworkBitmap = currentSong?.albumArtwork?.toBitmap(context)
            val bitmapBytes = artworkBitmap?.toBase64()

            for (id in GlanceAppWidgetManager(context).getGlanceIds(HorizontalControllerWidget::class.java)) {
                updateAppWidgetState(context, id) { pref ->
                    pref[longPreferencesKey(KEY_CURRENT_SONG_ID)] = currentSong?.id ?: 0L
                    pref[stringPreferencesKey(KEY_CURRENT_SONG_TITLE)] = currentSong?.title ?: context.getString(R.string.music_unknown_title)
                    pref[stringPreferencesKey(KEY_CURRENT_SONG_ARTIST)] = currentSong?.artist ?: context.getString(R.string.music_unknown_artist)
                    pref[stringPreferencesKey(KEY_CURRENT_SONG_ARTWORK)] = bitmapBytes.orEmpty()
                    pref[booleanPreferencesKey(KEY_IS_PLAYING)] = isPlaying
                    pref[booleanPreferencesKey(KEY_IS_PLUS_USER)] = isPlusUser
                }

                HorizontalControllerWidget().update(context, id)
            }

            latestIsPlaying = isPlaying
            latestIsPlusUser = isPlusUser
        }
    }

    private fun Bitmap.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()
        return android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
    }

    companion object {
        private const val ACTION_REQUEST_UPDATE = "action_request_update"

        fun createUpdateIntent(
            context: Context,
            isPlaying: Boolean,
            isPlusUser: Boolean,
        ): Intent {
            return Intent(context, HorizontalControllerWidgetReceiver::class.java).apply {
                action = ACTION_REQUEST_UPDATE
                putExtra(KEY_IS_PLAYING, isPlaying)
                putExtra(KEY_IS_PLUS_USER, isPlusUser)
            }
        }
    }
}

private suspend fun Artwork.toBitmap(context: Context): Bitmap? {
    try {
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
    } catch (e: Throwable) {
        Timber.e(e)
        return null
    }
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
