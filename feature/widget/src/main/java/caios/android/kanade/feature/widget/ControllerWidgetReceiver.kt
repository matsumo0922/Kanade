package caios.android.kanade.feature.widget

import android.app.PendingIntent
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
import caios.android.kanade.core.model.player.PlayerState
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.UserDataRepository
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
import kotlin.io.encoding.ExperimentalEncodingApi

@AndroidEntryPoint
class ControllerWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var userDataRepository: UserDataRepository

    @Inject
    lateinit var musicController: MusicController

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val glanceAppWidget: GlanceAppWidget
        get() = ControllerWidget()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateWidgets(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_REQUEST_UPDATE) {
            updateWidgets(context)
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun updateWidgets(context: Context) {
        scope.launch {
            val currentSong = musicController.currentSong.value
            val currentPlayerState = musicController.playerState.value

            val artworkBitmap = currentSong?.albumArtwork?.toBitmap(context)
            val bitmapBytes = artworkBitmap?.toBase64()

            for (id in GlanceAppWidgetManager(context).getGlanceIds(ControllerWidget::class.java)) {
                updateAppWidgetState(context, id) { pref ->
                    pref[longPreferencesKey(ControllerWidget.KEY_CURRENT_SONG_ID)] = currentSong?.id ?: 0L
                    pref[stringPreferencesKey(ControllerWidget.KEY_CURRENT_SONG_TITLE)] = currentSong?.title ?: context.getString(R.string.music_unknown_title)
                    pref[stringPreferencesKey(ControllerWidget.KEY_CURRENT_SONG_ARTIST)] = currentSong?.artist ?: context.getString(R.string.music_unknown_artist)
                    pref[stringPreferencesKey(ControllerWidget.KEY_CURRENT_SONG_ARTWORK)] = bitmapBytes.orEmpty()
                    pref[booleanPreferencesKey(ControllerWidget.KEY_IS_PLAYING)] = (currentPlayerState == PlayerState.Playing)
                    //pref[booleanPreferencesKey(ControllerWidget.KEY_IS_PLUS_USER)] = userData.isPlusMode || userData.isDeveloperMode
                }

                ControllerWidget().update(context, id)
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun Bitmap.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()
        return android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
    }

    companion object {
        private const val ACTION_REQUEST_UPDATE = "action_request_update"

        // 定期更新用
        fun createUpdatePendingIntent(context: Context): PendingIntent {
            val updateIntent = createUpdateIntent(context)
            val flags = PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE

            return PendingIntent.getBroadcast(context, 1, updateIntent, flags)
        }

        // 手動更新用
        fun createUpdateIntent(context: Context): Intent {
            Timber.d("Create update intent.")
            return Intent(context, ControllerWidgetReceiver::class.java).apply {
                action = ACTION_REQUEST_UPDATE
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
