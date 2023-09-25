package caios.android.kanade.feature.widget.items

import android.content.Intent
import android.graphics.drawable.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerState

@Composable
internal fun ControllerWidgetScreen(
    userData: UserData,
    currentSong: Song?,
    playerState: PlayerState,
    modifier: GlanceModifier = GlanceModifier,
) {
    val context = LocalContext.current
    val playPauseIcon = when (playerState) {
        PlayerState.Playing -> Icon.createWithResource(context, R.drawable.vec_play)
        PlayerState.Paused -> Icon.createWithResource(context, R.drawable.vec_pause)
        else -> Icon.createWithResource(context, R.drawable.vec_play)
    }
    val mainIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        putExtra("notify", true)
    }

    Row(
        modifier = modifier
            .background(GlanceTheme.colors.surface)
            .clickable(androidx.glance.appwidget.action.actionStartActivity(mainIntent ?: Intent())),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlanceArtwork(
            modifier = GlanceModifier.size(90.dp),
            artwork = currentSong?.albumArtwork ?: caios.android.kanade.core.model.music.Artwork.Unknown,
        )

        Column(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = GlanceModifier.fillMaxWidth(),
                text = currentSong?.title ?: stringResource(R.string.music_unknown_title),
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
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
                        .size(32.dp)
                        .clickable { },
                    provider = ImageProvider(Icon.createWithResource(context, R.drawable.vec_skip_to_previous)),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
                )

                Spacer(modifier = GlanceModifier.width(12.dp))

                Image(
                    modifier = GlanceModifier
                        .size(40.dp)
                        .clickable { },
                    provider = ImageProvider(playPauseIcon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
                )

                Spacer(modifier = GlanceModifier.width(12.dp))

                Image(
                    modifier = GlanceModifier
                        .size(32.dp)
                        .clickable { },
                    provider = ImageProvider(Icon.createWithResource(context, R.drawable.vec_skip_to_next)),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
                )
            }
        }
    }
}
