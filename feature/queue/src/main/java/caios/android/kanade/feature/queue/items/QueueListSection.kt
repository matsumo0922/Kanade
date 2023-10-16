package caios.android.kanade.feature.queue.items

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.LocalSystemBars
import caios.android.kanade.core.model.music.QueueItem
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.music.IndexedSongHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun QueueListSection(
    state: ReorderableLazyListState,
    queue: ImmutableList<QueueItem>,
    index: Int,
    onDeleteItem: (QueueItem) -> Boolean,
    onSkipToQueue: (Int) -> Unit,
    onClickSongMenu: (Song) -> Unit,
    modifier: Modifier = Modifier,
) {
    val systemBars = LocalSystemBars.current

    fun getItemIndex(item: QueueItem): Int {
        return queue.indexOfFirst { it.index == item.index }
    }

    Box(modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .reorderable(state)
                .detectReorderAfterLongPress(state),
            state = state.listState,
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = systemBars.bottom,
            ),
        ) {
            items(
                items = queue,
                key = { item -> item.index },
            ) { item ->
                ReorderableItem(
                    reorderableState = state,
                    key = "${item.index}",
                ) { isDragging ->
                    val dismissState = rememberDismissState(
                        confirmValueChange = {
                            if (it == DismissValue.Default) return@rememberDismissState false
                            onDeleteItem(item)
                        },
                    )
                    val elevation = animateDpAsState(
                        targetValue = if (isDragging) 16.dp else 0.dp,
                        label = "elevation",
                    )
                    val background = animateColorAsState(
                        targetValue = if (index == getItemIndex(item)) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                        label = "background",
                    )

                    SwipeToDismiss(
                        modifier = Modifier.animateItemPlacement(),
                        state = dismissState,
                        background = { },
                        dismissContent = {
                            IndexedSongHolder(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .zIndex(if (isDragging) 1f else 0f)
                                    .background(background.value)
                                    .shadow(elevation.value),
                                song = item.song,
                                index = getItemIndex(item),
                                state = state,
                                onClickHolder = onSkipToQueue,
                                onClickMenu = onClickSongMenu,
                            )
                        },
                        directions = setOf(DismissDirection.EndToStart),
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            Color.Transparent,
                        ),
                    ),
                ),
        )
    }
}

@Preview
@Composable
private fun QueueListSectionPreview() {
    KanadeBackground(Modifier.background(MaterialTheme.colorScheme.surface)) {
        QueueListSection(
            modifier = Modifier.fillMaxWidth(),
            queue = Song.dummies(10).mapIndexed { index, song -> QueueItem(song, index) }.toImmutableList(),
            index = 2,
            state = rememberReorderableLazyListState(onMove = { _, _ -> }),
            onSkipToQueue = { },
            onDeleteItem = { true },
            onClickSongMenu = { },
        )
    }
}
