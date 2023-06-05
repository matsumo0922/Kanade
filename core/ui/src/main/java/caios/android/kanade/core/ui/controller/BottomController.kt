package caios.android.kanade.core.ui.controller

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.KanadeTheme
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.ui.music.Artwork

@Composable
fun BottomController(
    onClickPlay: () -> Unit,
    onClickPause: () -> Unit,
    onClickSkipToNext: () -> Unit,
    onClickSkipToPrevious: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier) {
        val (indicator, artwork, title, artist, buttons) = createRefs()

        LinearProgressIndicator(
            modifier = Modifier.constrainAs(indicator) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

                width = Dimension.fillToConstraints
            },
        )

        Artwork(
            modifier = Modifier
                .constrainAs(artwork) {
                    top.linkTo(indicator.bottom)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                }
                .aspectRatio(1f),
            artwork = Artwork.Internal("ABC"),
        )

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(artwork.end, 16.dp)
                end.linkTo(buttons.start, 16.dp)
                bottom.linkTo(artist.top)

                width = Dimension.fillToConstraints
            },
            text = "Title",
            style = MaterialTheme.typography.bodyLarge,
        )

        Text(
            modifier = Modifier.constrainAs(artist) {
                top.linkTo(title.bottom)
                start.linkTo(artwork.end, 16.dp)
                end.linkTo(buttons.start, 16.dp)
                bottom.linkTo(parent.bottom, 16.dp)

                width = Dimension.fillToConstraints
            },
            text = "Artist",
            style = MaterialTheme.typography.bodyMedium,
        )

        Row(
            modifier = Modifier.constrainAs(buttons) {
                top.linkTo(parent.top)
                end.linkTo(parent.end, 8.dp)
                bottom.linkTo(parent.bottom)
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onClickSkipToNext() }
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = LocalContentColor.current,
                        shape = RoundedCornerShape(24.dp),
                    )
                    .padding(4.dp),
                imageVector = Icons.Filled.SkipPrevious,
                contentDescription = "Skip to previous",
            )

            Icon(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(27.dp))
                    .clickable { onClickPlay() }
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = LocalContentColor.current,
                        shape = RoundedCornerShape(24.dp),
                    )
                    .padding(4.dp),
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play or Pause",
            )

            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onClickSkipToNext() }
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = LocalContentColor.current,
                        shape = RoundedCornerShape(24.dp),
                    )
                    .padding(4.dp),
                imageVector = Icons.Filled.SkipNext,
                contentDescription = "Skip to next",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    KanadeTheme {
        KanadeBackground {
            BottomController(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                onClickPlay = {},
                onClickPause = {},
                onClickSkipToNext = {},
                onClickSkipToPrevious = {},
            )
        }
    }
}
