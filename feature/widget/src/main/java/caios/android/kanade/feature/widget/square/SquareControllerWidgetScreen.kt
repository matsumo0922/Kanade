package caios.android.kanade.feature.widget.square

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import caios.android.kanade.core.design.R

@Composable
internal fun SquareControllerWidgetScreen(
    songTitle: String,
    songArtwork: Bitmap?,
    isPlaying: Boolean,
    isPlusUser: Boolean,
    modifier: GlanceModifier = GlanceModifier,
) {
    val context = LocalContext.current
    val widgetSize = LocalSize.current
    val playPauseIcon = Icon.createWithResource(context, if (isPlaying) R.drawable.vec_pause else R.drawable.vec_play)
    val mainIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        putExtra("notify", true)
    }
    val plusIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        putExtra("plus", true)
    }


}
