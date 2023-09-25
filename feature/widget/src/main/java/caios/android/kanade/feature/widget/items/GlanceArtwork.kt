package caios.android.kanade.feature.widget.items

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.LocalContext
import androidx.glance.layout.ContentScale
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Artwork
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import timber.log.Timber

@Composable
fun GlanceArtwork(
    artwork: Artwork,
    modifier: GlanceModifier = GlanceModifier,
) {
    when (artwork) {
        is Artwork.MediaStore -> ArtworkFromMediaStore(artwork, modifier)
        is Artwork.Web -> ArtworkFromWeb(artwork, modifier)
        else -> ArtworkFromUnknown(modifier)
    }
}

@Composable
private fun ArtworkFromWeb(
    artwork: Artwork.Web,
    modifier: GlanceModifier = GlanceModifier,
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(artwork.url) {
        try {
            val request = ImageRequest.Builder(context)
                .data(artwork.url)
                .allowHardware(false)
                .build()
            val result = (ImageLoader(context).execute(request) as? SuccessResult)?.drawable

            bitmap = (result as? BitmapDrawable)?.bitmap
        } catch (e: Exception) {
            Timber.w(e)
        }
    }

    Image(
        modifier = modifier,
        provider = androidx.glance.ImageProvider(bitmap?.let { Icon.createWithBitmap(it) } ?: Icon.createWithResource(context, R.drawable.im_default_artwork)),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun ArtworkFromMediaStore(
    artwork: Artwork.MediaStore,
    modifier: GlanceModifier = GlanceModifier,
) {
    Image(
        modifier = modifier,
        provider = androidx.glance.ImageProvider(Icon.createWithContentUri(artwork.uri)),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun ArtworkFromUnknown(
    modifier: GlanceModifier = GlanceModifier,
) {
    Image(
        modifier = modifier,
        provider = androidx.glance.ImageProvider(R.drawable.im_default_artwork),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}
