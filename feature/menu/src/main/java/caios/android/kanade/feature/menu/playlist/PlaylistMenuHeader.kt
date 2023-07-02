package caios.android.kanade.feature.menu.playlist

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
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.ui.music.MultiArtwork
import caios.android.kanade.core.ui.util.marquee

@Composable
internal fun PlaylistMenuHeader(
    playlist: Playlist,
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
            modifier = Modifier.constrainAs(artwork) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 16.dp)
                bottom.linkTo(parent.bottom, 16.dp)
            },
            shape = RoundedCornerShape(4.dp),
        ) {
            MultiArtwork(
                modifier = Modifier.size(48.dp),
                songs = playlist.songs,
            )
        }

        Text(
            modifier = Modifier
                .marquee()
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(artwork.end, 8.dp)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(artist.top)

                    width = Dimension.fillToConstraints
                },
            text = playlist.name,
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
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(parent.bottom)

                    width = Dimension.fillToConstraints
                },
            text = stringResource(R.string.unit_song, playlist.items.size),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaylistMenuHeaderPreview() {
    KanadeBackground {
        PlaylistMenuHeader(
            modifier = Modifier.fillMaxWidth(),
            playlist = Playlist.dummy(),
        )
    }
}
