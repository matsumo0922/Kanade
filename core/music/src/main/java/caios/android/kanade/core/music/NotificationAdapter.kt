package caios.android.kanade.core.music

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.compose.ui.graphics.toArgb
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import caios.android.kanade.core.design.databinding.LayoutDefaultArtworkBinding
import caios.android.kanade.core.design.theme.Blue40
import caios.android.kanade.core.design.theme.Green40
import caios.android.kanade.core.design.theme.Orange40
import caios.android.kanade.core.design.theme.Purple40
import caios.android.kanade.core.design.theme.Teal40
import caios.android.kanade.core.model.music.Artwork
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@UnstableApi
class NotificationAdapter(
    private val context: Context,
    private val pendingIntent: PendingIntent?,
    private val scope: CoroutineScope,
) : PlayerNotificationManager.MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): CharSequence {
        return player.mediaMetadata.albumTitle ?: "Unknown"
    }

    override fun getCurrentContentText(player: Player): CharSequence {
        return player.mediaMetadata.displayTitle ?: "Unknown"
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return pendingIntent
    }

    override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
        player.mediaMetadata.extras?.let {
            scope.launch {
                val artwork = it.getParcelable<Artwork>("artwork") ?: return@launch
                val bitmap = artwork.toBitmap(context) ?: return@launch

                callback.onBitmap(bitmap)
            }
        }

        return null
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

    private fun View.toBitmap(): Bitmap? {
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
