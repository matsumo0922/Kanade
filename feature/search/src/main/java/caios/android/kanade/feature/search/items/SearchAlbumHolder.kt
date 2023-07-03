package caios.android.kanade.feature.search.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import caios.android.kanade.feature.search.util.getAnnotatedString

@Composable
fun SearchAlbumHolder(
    album: Album,
    range: IntRange,
    onClickHolder: () -> Unit,
    onClickMenu: (Album) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier.clickable { onClickHolder.invoke() }) {
        val (artwork, title, artist, duration, menu) = createRefs()

        createVerticalChain(
            title.withChainParams(bottomMargin = 2.dp),
            artist.withChainParams(topMargin = 2.dp),
            chainStyle = ChainStyle.Packed,
        )

        Card(
            modifier = Modifier
                .size(56.dp)
                .constrainAs(artwork) {
                    top.linkTo(parent.top, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                },
            shape = RoundedCornerShape(8.dp),
        ) {
            Artwork(
                modifier = Modifier.fillMaxSize(),
                artwork = album.artwork,
            )
        }

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(artwork.top)
                start.linkTo(artwork.end, 16.dp)
                end.linkTo(menu.start, 8.dp)

                width = Dimension.fillToConstraints
            },
            text = getAnnotatedString(album.album, range),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier = Modifier.constrainAs(artist) {
                top.linkTo(title.bottom)
                start.linkTo(title.start)
                end.linkTo(duration.start, 16.dp)

                width = Dimension.fillToConstraints
            },
            text = album.artist,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier = Modifier.constrainAs(duration) {
                top.linkTo(artist.top)
                bottom.linkTo(artist.bottom)
                end.linkTo(menu.start, 8.dp)
            },
            text = stringResource(R.string.unit_song, album.songs.size),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Icon(
            modifier = Modifier
                .size(32.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(50))
                .constrainAs(menu) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, 12.dp)
                }
                .clickable { onClickMenu.invoke(album) }
                .padding(4.dp),
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun SearchAlbumHolderPreview() {
    KanadeBackground(Modifier.background(MaterialTheme.colorScheme.surface)) {
        SearchAlbumHolder(
            modifier = Modifier.fillMaxWidth(),
            album = Album.dummy(),
            range = 4..5,
            onClickMenu = {},
            onClickHolder = {},
        )
    }
}
