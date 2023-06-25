package caios.android.kanade.feature.menu.album

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.ui.music.Artwork
import caios.android.kanade.core.ui.util.marquee

@Composable
internal fun AlbumMenuHeader(
    album: Album,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier) {
        val (artwork, title, artist, count) = createRefs()

        createVerticalChain(
            title.withChainParams(bottomMargin = 2.dp),
            artist.withChainParams(topMargin = 2.dp),
            chainStyle = ChainStyle.Packed,
        )

        Card(
            modifier = Modifier.constrainAs(artwork) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 16.dp)
                bottom.linkTo(parent.bottom, 16.dp)
            },
            shape = RoundedCornerShape(4.dp),
        ) {
            Artwork(
                modifier = Modifier.size(48.dp),
                artwork = album.artwork,
            )
        }

        Text(
            modifier = Modifier
                .marquee()
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(artwork.end, 8.dp)
                    end.linkTo(count.start, 8.dp)
                    bottom.linkTo(artist.top)

                    width = Dimension.fillToConstraints
                },
            text = album.album,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier = Modifier
                .marquee()
                .constrainAs(artist) {
                    top.linkTo(title.bottom)
                    start.linkTo(artwork.end, 8.dp)
                    end.linkTo(count.start, 8.dp)
                    bottom.linkTo(parent.bottom)

                    width = Dimension.fillToConstraints
                },
            text = album.artist,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier = Modifier.constrainAs(count) {
                top.linkTo(artist.top)
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(artist.bottom)
            },
            text = stringResource(R.string.unit_song, album.songs.size),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlbumMenuHeaderPreview() {
    KanadeBackground {
        AlbumMenuHeader(
            modifier = Modifier.fillMaxWidth(),
            album = Album.dummy(),
        )
    }
}
