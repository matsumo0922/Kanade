package caios.android.kanade.core.ui.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.Blue40
import caios.android.kanade.core.design.theme.Green40
import caios.android.kanade.core.design.theme.Orange40
import caios.android.kanade.core.design.theme.Purple40
import caios.android.kanade.core.design.theme.Teal40
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.ui.util.extraSize
import coil.compose.AsyncImage

@Composable
fun Artwork(
    artwork: Artwork,
    modifier: Modifier = Modifier,
) {
    when (artwork) {
        is Artwork.Internal -> ArtworkFromInternal(modifier, artwork)
        is Artwork.MediaStore -> ArtworkFromMediaStore(modifier, artwork)
        is Artwork.Web -> ArtworkFromWeb(modifier, artwork)
        is Artwork.Unknown -> ArtworkFromUnknown(modifier)
    }
}

@Composable
private fun ArtworkFromWeb(
    artwork: Artwork.Web,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f),
        model = artwork.url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun ArtworkFromMediaStore(
    artwork: Artwork.MediaStore,
    modifier: Modifier,
) {
    AsyncImage(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f),
        model = artwork.uri,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun ArtworkFromUnknown(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f),
        painter = painterResource(R.drawable.im_default_artwork),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun ArtworkFromInternal(
    artwork: Artwork.Internal,
    modifier: Modifier,
) {
    val density = LocalDensity.current

    val name = remember { artwork.name.replace(Regex("\\s+"), "") }
    val char1 = remember { name.elementAtOrNull(0)?.uppercase() ?: "?" }
    val char2 = remember { name.elementAtOrNull(1)?.uppercase() ?: char1 }

    val backgroundColor = when (name.toList().sumOf { it.code } % 5) {
        0 -> Blue40
        1 -> Green40
        2 -> Orange40
        3 -> Purple40
        4 -> Teal40
        else -> throw IllegalArgumentException("Unknown album name.")
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .background(backgroundColor)
            .clipToBounds(),
    ) {
        val boxHeight = with(LocalDensity.current) { constraints.maxHeight.toDp() }
        val fontSize = with(density) { (boxHeight * 1.4f).toSp() }
        val xOffset = boxHeight * 0.15f
        val yOffset = boxHeight * 0.05f
        val xOffsetPlus = boxHeight * 0.02f
        val yOffsetPlus = boxHeight * 0.02f

        Text(
            modifier = Modifier
                .fillMaxHeight()
                .extraSize(0.3f, 0.3f)
                .aspectRatio(1f)
                .align(Alignment.Center)
                .offset(-xOffset + xOffsetPlus, -yOffset + yOffsetPlus)
                .rotate(20f),
            text = char1,
            style = TextStyle(
                color = Color.White.copy(alpha = 0.3f),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                platformStyle = PlatformTextStyle(includeFontPadding = false),
            ),
            maxLines = 1,
            textAlign = TextAlign.Center,
        )

        Text(
            modifier = Modifier
                .fillMaxHeight()
                .extraSize(0.3f, 0.3f)
                .aspectRatio(1f)
                .align(Alignment.Center)
                .offset(xOffset + xOffsetPlus, yOffset + yOffsetPlus)
                .rotate(20f),
            text = char2,
            style = TextStyle(
                color = Color.White.copy(alpha = 0.3f),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                platformStyle = PlatformTextStyle(includeFontPadding = false),
            ),
            maxLines = 1,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Artwork(
        modifier = Modifier,
        artwork = Artwork.Internal("ABC"),
    )
}
