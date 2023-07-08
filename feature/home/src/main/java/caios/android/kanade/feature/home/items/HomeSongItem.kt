package caios.android.kanade.feature.home.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.music.Artwork

@Composable
internal fun HomeSongItem(
    song: Song,
    onClickHolder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.padding(6.dp)) {
        Card(
            modifier = Modifier.clickable { onClickHolder.invoke() },
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        ) {
            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (artwork, title, artist, shadow) = createRefs()

                createVerticalChain(
                    title.withChainParams(bottomMargin = 2.dp),
                    artist.withChainParams(topMargin = 2.dp),
                    chainStyle = ChainStyle.Packed,
                )

                Artwork(
                    modifier = Modifier
                        .size(96.dp)
                        .constrainAs(artwork) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                    artwork = song.albumArtwork,
                )

                Box(
                    modifier = Modifier
                        .constrainAs(shadow) {
                            top.linkTo(artwork.top)
                            end.linkTo(artwork.end)
                            bottom.linkTo(artwork.bottom)

                            height = Dimension.fillToConstraints
                            width = Dimension.value(96.dp)
                        }
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surfaceContainerHigh,
                                ),
                            ),
                        ),
                )

                Text(
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(artwork.top)
                        start.linkTo(artwork.end, 8.dp)
                        end.linkTo(parent.end, 16.dp)
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
                    modifier = Modifier.constrainAs(artist) {
                        top.linkTo(title.bottom)
                        start.linkTo(artwork.end, 8.dp)
                        end.linkTo(parent.end, 16.dp)
                        bottom.linkTo(artwork.bottom)

                        width = Dimension.fillToConstraints
                    },
                    text = song.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeSongItemPreview() {
    HomeSongItem(
        modifier = Modifier.width(224.dp),
        song = Song.dummy(),
        onClickHolder = {},
    )
}
