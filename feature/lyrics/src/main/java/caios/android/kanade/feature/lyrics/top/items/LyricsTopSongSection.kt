package caios.android.kanade.feature.lyrics.top.items

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.music.Artwork

@Composable
internal fun LyricsTopSongSection(
    song: Song,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier) {
        val (artwork, title, artist) = createRefs()

        createVerticalChain(
            title.withChainParams(bottomMargin = 2.dp),
            artist.withChainParams(topMargin = 2.dp),
            chainStyle = ChainStyle.Packed,
        )

        Card(
            modifier = Modifier
                .size(64.dp)
                .constrainAs(artwork) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start, 24.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                },
            shape = RoundedCornerShape(8.dp),
        ) {
            Artwork(
                modifier = Modifier.fillMaxSize(),
                artwork = song.albumArtwork,
            )
        }

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(artwork.top)
                bottom.linkTo(artist.top)
                start.linkTo(artwork.end, 24.dp)
                end.linkTo(parent.end, 24.dp)

                width = Dimension.fillToConstraints
            },
            text = song.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier = Modifier.constrainAs(artist) {
                top.linkTo(title.bottom)
                bottom.linkTo(artwork.bottom)
                start.linkTo(artwork.end, 24.dp)
                end.linkTo(parent.end, 24.dp)

                width = Dimension.fillToConstraints
            },
            text = song.artist,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LyricsTopSongSectionPreview() {
    KanadeBackground {
        LyricsTopSongSection(
            modifier = Modifier.fillMaxWidth(),
            song = Song.dummy(),
        )
    }
}
