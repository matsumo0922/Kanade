package caios.android.kanade.feature.menu.song

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
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
import caios.android.kanade.core.design.theme.Red40
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.music.Artwork
import caios.android.kanade.core.ui.util.marquee

@Composable
internal fun SongMenuHeader(
    song: Song,
    isFavorite: Boolean,
    onClickFavorite: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier) {
        val (artwork, title, artist, duration, favorite) = createRefs()

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
                artwork = song.albumArtwork,
            )
        }

        Text(
            modifier = Modifier
                .marquee()
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(artwork.end, 8.dp)
                    end.linkTo(favorite.start, 8.dp)
                    bottom.linkTo(artist.top)

                    width = Dimension.fillToConstraints
                },
            text = song.title,
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
                    end.linkTo(duration.start, 8.dp)
                    bottom.linkTo(parent.bottom)

                    width = Dimension.fillToConstraints
                },
            text = song.artist,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier = Modifier.constrainAs(duration) {
                top.linkTo(artist.top)
                end.linkTo(favorite.start, 16.dp)
                bottom.linkTo(artist.bottom)
            },
            text = song.durationString,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Icon(
            modifier = Modifier
                .constrainAs(favorite) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(parent.bottom)
                }
                .clip(RoundedCornerShape(50))
                .clickable { onClickFavorite(!isFavorite) }
                .padding(4.dp),
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = if (isFavorite) Red40 else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SongMenuHeaderPreview1() {
    KanadeBackground {
        SongMenuHeader(
            modifier = Modifier.fillMaxWidth(),
            song = Song.dummy(),
            isFavorite = true,
            onClickFavorite = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SongMenuHeaderPreview2() {
    KanadeBackground {
        SongMenuHeader(
            modifier = Modifier.fillMaxWidth(),
            song = Song.dummy(),
            isFavorite = false,
            onClickFavorite = { },
        )
    }
}
