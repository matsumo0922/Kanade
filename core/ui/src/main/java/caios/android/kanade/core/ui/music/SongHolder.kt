package caios.android.kanade.core.ui.music

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Song

@Composable
fun SongHolder(
    song: Song,
    onClickHolder: () -> Unit,
    onClickMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier.clickable { onClickHolder.invoke() }) {
        val (artwork, title, artist, duration, menu) = createRefs()

        createVerticalChain(
            title.withChainParams(bottomMargin = 2.dp),
            artist.withChainParams(topMargin = 2.dp),
            chainStyle = ChainStyle.Packed,
        )

        Artwork(
            modifier = Modifier
                .size(64.dp)
                .aspectRatio(1f)
                .constrainAs(artwork) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            artwork = song.albumArtwork,
        )

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(artwork.top)
                bottom.linkTo(artist.top)
                start.linkTo(artwork.end, 16.dp)
                end.linkTo(menu.start, 16.dp)

                width = Dimension.fillToConstraints
            },
            text = song.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier = Modifier.constrainAs(artist) {
                top.linkTo(title.bottom)
                bottom.linkTo(artwork.bottom)
                start.linkTo(artwork.end, 16.dp)
                end.linkTo(duration.start, 16.dp)

                width = Dimension.fillToConstraints
            },
            text = song.artist,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier = Modifier.constrainAs(duration) {
                top.linkTo(artist.top)
                bottom.linkTo(artist.bottom)
                end.linkTo(menu.start, 16.dp)
            },
            text = song.durationString,
            style = MaterialTheme.typography.bodyMedium,
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
                    end.linkTo(parent.end, 16.dp)
                }
                .clickable { onClickMenu.invoke() }
                .padding(4.dp),
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MusicHolderPreview() {
    KanadeBackground(Modifier.wrapContentSize()) {
        SongHolder(
            modifier = Modifier.fillMaxWidth(),
            song = Song.dummy(),
            onClickHolder = { },
            onClickMenu = { },
        )
    }
}
