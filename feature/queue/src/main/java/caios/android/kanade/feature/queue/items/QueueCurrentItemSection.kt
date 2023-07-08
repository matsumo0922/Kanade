package caios.android.kanade.feature.queue.items

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.Red40
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.music.Artwork
import caios.android.kanade.core.ui.util.marquee
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
internal fun QueueCurrentItemSection(
    song: Song,
    isPlaying: Boolean,
    onClickHolder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var atEnd by remember { mutableStateOf(false) }
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.av_equalizer)

    suspend fun runAnimation() {
        while (isPlaying) {
            atEnd = !atEnd
            delay(2000)
        }
    }

    LaunchedEffect(isPlaying) {
        runAnimation()
    }

    ConstraintLayout(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .clickable { onClickHolder.invoke() },
    ) {
        val (artwork, title, artist, icon) = createRefs()

        Card(
            modifier = Modifier
                .size(56.dp)
                .constrainAs(artwork) {
                    top.linkTo(parent.top, 16.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                    start.linkTo(parent.start, 16.dp)
                },
            shape = RoundedCornerShape(8.dp),
        ) {
            Artwork(
                modifier = Modifier.fillMaxSize(),
                artwork = song.albumArtwork,
            )
        }

        Text(
            modifier = Modifier
                .marquee()
                .constrainAs(title) {
                    top.linkTo(artwork.top)
                    bottom.linkTo(artist.top)
                    start.linkTo(artwork.end, 8.dp)
                    end.linkTo(icon.start, 8.dp)

                    width = Dimension.fillToConstraints
                },
            text = song.title,
            style = MaterialTheme.typography.bodyLarge.bold(),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )

        Text(
            modifier = Modifier
                .marquee()
                .constrainAs(artist) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(artwork.bottom)
                    start.linkTo(artwork.end, 8.dp)
                    end.linkTo(icon.start, 8.dp)

                    width = Dimension.fillToConstraints
                },
            text = song.artist,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )

        Icon(
            modifier = Modifier
                .size(40.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, 16.dp)
                },
            painter = rememberAnimatedVectorPainter(image, atEnd),
            contentDescription = null,
            tint = Red40,
        )
    }
}

@Preview
@Composable
private fun QueueCurrentItemSectionPreview() {
    KanadeBackground(Modifier.background(MaterialTheme.colorScheme.background)) {
        QueueCurrentItemSection(
            modifier = Modifier.fillMaxWidth(),
            song = Song.dummy(),
            isPlaying = true,
            onClickHolder = {},
        )
    }
}
