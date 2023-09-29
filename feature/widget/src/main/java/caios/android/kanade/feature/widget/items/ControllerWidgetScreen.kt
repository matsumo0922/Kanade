package caios.android.kanade.feature.widget.items

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionSendBroadcast
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.color.ColorProviders
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import caios.android.kanade.core.design.R
import caios.android.kanade.core.music.MusicButtonReceiver
import caios.android.kanade.feature.widget.ControllerWidget.Companion.ACTION_PAUSE
import caios.android.kanade.feature.widget.ControllerWidget.Companion.ACTION_PLAY
import caios.android.kanade.feature.widget.ControllerWidget.Companion.ACTION_SKIP_TO_NEXT
import caios.android.kanade.feature.widget.ControllerWidget.Companion.ACTION_SKIP_TO_PREVIOUS
import kotlin.math.ln

@Composable
internal fun ControllerWidgetScreen(
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

    Box {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surfaceColorAtElevation(context, 6.dp))
                .clickable(androidx.glance.appwidget.action.actionStartActivity(mainIntent ?: Intent())),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = GlanceModifier.size(widgetSize.height),
                provider = ImageProvider(songArtwork?.let { Icon.createWithBitmap(it) } ?: Icon.createWithResource(context, R.drawable.im_default_artwork)),
                contentDescription = null,
            )

            Column(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = GlanceModifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    text = songTitle,
                    style = TextStyle(
                        color = GlanceTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                )

                Row(
                    modifier = GlanceModifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = GlanceModifier
                            .size(44.dp)
                            .cornerRadius(22.dp)
                            .clickable(actionSendBroadcast(ACTION_SKIP_TO_PREVIOUS, ComponentName(context, MusicButtonReceiver::class.java)))
                            .padding(4.dp),
                        provider = ImageProvider(Icon.createWithResource(context, R.drawable.vec_skip_to_previous)),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
                    )

                    Spacer(modifier = GlanceModifier.width(16.dp))

                    Image(
                        modifier = GlanceModifier
                            .size(52.dp)
                            .cornerRadius(26.dp)
                            .clickable(
                                if (isPlaying) {
                                    actionSendBroadcast(ACTION_PAUSE, ComponentName(context, MusicButtonReceiver::class.java))
                                } else {
                                    actionSendBroadcast(ACTION_PLAY, ComponentName(context, MusicButtonReceiver::class.java))
                                }
                            )
                            .padding(4.dp),
                        provider = ImageProvider(playPauseIcon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
                    )

                    Spacer(modifier = GlanceModifier.width(16.dp))

                    Image(
                        modifier = GlanceModifier
                            .size(44.dp)
                            .cornerRadius(22.dp)
                            .clickable(actionSendBroadcast(ACTION_SKIP_TO_NEXT, ComponentName(context, MusicButtonReceiver::class.java)))
                            .padding(4.dp),
                        provider = ImageProvider(Icon.createWithResource(context, R.drawable.vec_skip_to_next)),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
                    )
                }
            }
        }

        if (!isPlusUser) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(GlanceTheme.colors.errorContainer.getColor(context).copy(alpha = 0.9f))
                    .clickable(androidx.glance.appwidget.action.actionStartActivity(plusIntent ?: Intent())),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = GlanceModifier.padding(16.dp),
                    text = context.getString(R.string.widget_error_require_plus),
                    style = TextStyle(
                        color = GlanceTheme.colors.onErrorContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }
}

private fun ColorProviders.surfaceColorAtElevation(context: Context, elevation: Dp): Color {
    if (elevation == 0.dp) return surface.getColor(context)
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return primary.getColor(context).copy(alpha = alpha).compositeOver(surface.getColor(context))
}
