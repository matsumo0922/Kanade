package caios.android.kanade.feature.queue.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.design.theme.excludeFontPadding
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.music.Artwork
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import java.util.Locale

@Composable
internal fun QueueListItem(
    song: Song,
    index: Int,
    state: ReorderableLazyListState,
    onClickHolder: (Int) -> Unit,
    onClickMenu: (Song) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier.clickable { onClickHolder.invoke(index) }) {
        val (artwork, number, title, artist, duration, menu, handle) = createRefs()

        createVerticalChain(
            title.withChainParams(bottomMargin = 2.dp),
            artist.withChainParams(topMargin = 2.dp),
            chainStyle = ChainStyle.Packed,
        )

        Card(
            modifier = Modifier
                .size(48.dp)
                .constrainAs(artwork) {
                    top.linkTo(parent.top, 12.dp)
                    bottom.linkTo(parent.bottom, 12.dp)
                    start.linkTo(parent.start, 16.dp)
                },
            shape = RoundedCornerShape(8.dp),
        ) {
            Artwork(
                modifier = Modifier.fillMaxSize(),
                artwork = song.artwork,
            )
        }

        Card(
            modifier = Modifier.constrainAs(number) {
                top.linkTo(artwork.bottom)
                bottom.linkTo(artwork.bottom)
                start.linkTo(artwork.start)
                end.linkTo(artwork.end)
            },
            shape = RoundedCornerShape(50),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp),
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 7.dp),
                text = (index + 1).toString(),
                style = MaterialTheme.typography.bodySmall.bold().excludeFontPadding(),
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(artwork.top)
                start.linkTo(artwork.end, 16.dp)
                end.linkTo(menu.start, 8.dp)

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
                start.linkTo(title.start)
                end.linkTo(duration.start, 16.dp)

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
                bottom.linkTo(artist.bottom)
                end.linkTo(menu.start, 8.dp)
            },
            text = getDurationTime(song.duration),
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
                    end.linkTo(handle.start, 4.dp)
                }
                .clickable { onClickMenu.invoke(song) }
                .padding(4.dp),
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
        )

        Icon(
            modifier = Modifier
                .size(32.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(50))
                .constrainAs(handle) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, 12.dp)
                }
                .detectReorder(state)
                .padding(4.dp),
            imageVector = Icons.Default.DragHandle,
            contentDescription = null,
        )
    }
}

private fun getDurationTime(duration: Long): String {
    val second = duration / 1000
    val minute = second / 60
    val hour = minute / 60

    return if (hour > 0) {
        String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute % 60, second % 60)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minute, second % 60)
    }
}

@Preview
@Composable
private fun QueueListItemPreview() {
    KanadeBackground(Modifier.background(MaterialTheme.colorScheme.surface)) {
        QueueListItem(
            modifier = Modifier.fillMaxWidth(),
            song = Song.dummy(),
            state = rememberReorderableLazyListState(onMove = { _, _ -> }),
            index = 2,
            onClickMenu = {},
            onClickHolder = {},
        )
    }
}
