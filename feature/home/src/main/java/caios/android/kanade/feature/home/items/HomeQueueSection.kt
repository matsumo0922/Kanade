package caios.android.kanade.feature.home.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.model.music.Queue
import caios.android.kanade.core.model.music.Song

@Composable
internal fun HomeQueueSection(
    queue: Queue,
    onClickQueue: () -> Unit,
    onClickQueueItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()

    LaunchedEffect(queue.index) {
        if (queue.index >= 0) state.animateScrollToItem(queue.index)
    }

    Column(
        modifier = modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.home_title_queue),
                style = MaterialTheme.typography.titleMedium.bold(),
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.unit_song, queue.items.size),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable { onClickQueue.invoke() }
                    .padding(4.dp),
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            contentPadding = PaddingValues(horizontal = 12.dp),
        ) {
            itemsIndexed(
                items = queue.items,
                key = { index, song -> "queue-${song.id}-$index" },
            ) { index, song ->
                HomeSongItem(
                    modifier = Modifier.width(224.dp),
                    song = song,
                    onClickHolder = { onClickQueueItem.invoke(index) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeQueueSectionPreview() {
    KanadeBackground {
        HomeQueueSection(
            queue = Queue(
                items = Song.dummies(15),
                index = 2,
            ),
            onClickQueue = { },
            onClickQueueItem = {},
        )
    }
}
