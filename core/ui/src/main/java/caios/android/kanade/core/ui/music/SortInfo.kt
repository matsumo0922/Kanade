package caios.android.kanade.core.ui.music

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.end
import caios.android.kanade.core.model.Order
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption

@Composable
fun SortInfo(
    sortOrder: MusicOrder,
    itemSize: Int,
    onClickSort: (MusicOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.clickable { onClickSort.invoke(sortOrder) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = "タイトル",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = if (sortOrder.order == Order.ASC) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = null,
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = when (sortOrder.musicOrderOption) {
                is MusicOrderOption.Song -> stringResource(R.string.unit_song, itemSize)
                is MusicOrderOption.Artist -> stringResource(R.string.unit_artist, itemSize)
                is MusicOrderOption.Album -> stringResource(R.string.unit_album, itemSize)
                else -> stringResource(R.string.common_unknown)
            },
            style = MaterialTheme.typography.bodyMedium.end(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
fun PreviewSortInfo() {
    SortInfo(
        sortOrder = MusicOrder(
            order = Order.ASC,
            musicOrderOption = MusicOrderOption.Album.ARTIST,
        ),
        itemSize = 0,
        onClickSort = {},
    )
}
