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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.ui.music.Artwork
import caios.android.kanade.feature.search.util.getAnnotatedString

@Composable
fun SearchArtistHolder(
    artist: Artist,
    range: IntRange,
    onClickHolder: () -> Unit,
    onClickMenu: (Artist) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier.clickable { onClickHolder.invoke() }) {
        val (artwork, title, menu) = createRefs()

        Card(
            modifier = Modifier
                .size(48.dp)
                .constrainAs(artwork) {
                    top.linkTo(parent.top, 12.dp)
                    bottom.linkTo(parent.bottom, 12.dp)
                    start.linkTo(parent.start, 16.dp)
                },
            shape = RoundedCornerShape(50),
        ) {
            Artwork(
                modifier = Modifier.fillMaxSize(),
                artwork = artist.artwork,
            )
        }

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(artwork.top)
                bottom.linkTo(artwork.bottom)
                start.linkTo(artwork.end, 16.dp)
                end.linkTo(menu.start, 8.dp)

                width = Dimension.fillToConstraints
            },
            text = getAnnotatedString(artist.artist, range),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
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
                .clickable { onClickMenu.invoke(artist) }
                .padding(4.dp),
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun SearchArtistHolderPreview() {
    KanadeBackground(Modifier.background(MaterialTheme.colorScheme.surface)) {
        SearchArtistHolder(
            modifier = Modifier.fillMaxWidth(),
            artist = Artist.dummy(),
            range = 4..5,
            onClickMenu = {},
            onClickHolder = {},
        )
    }
}
