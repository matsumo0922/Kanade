package caios.android.kanade.feature.queue.items

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Song
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
    queue: ImmutableList<Song>,
    index: Int,
    onDeleteItem: (Int) -> Unit,
    onSkipToQueue: (Int) -> Unit,
    onClickSongMenu: (Song) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val navigationBarsPadding = WindowInsets.navigationBars.getBottom(density)

    Box(modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .reorderable(state)
                .detectReorderAfterLongPress(state),
            state = state.listState,
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = with(density) { navigationBarsPadding.toDp() },
            ),
        ) {
            itemsIndexed(
                items = queue,
                key = { _, item -> item.id },
            ) { i, item ->
                ReorderableItem(
                    reorderableState = state,
                    key = "${item.id}:$i",
                ) { isDragging ->
                    val dismissState = rememberDismissState(
                        confirmValueChange = {
                            if (i == index) {
                                false
                            } else {
                                onDeleteItem.invoke(it.ordinal)
                                true
                            }
                        },
                    )
                    val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                    val background = animateColorAsState(if (index == i) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface)

                    SwipeToDismiss(
                        modifier = Modifier.animateItemPlacement(),
                        state = dismissState,
                        background = {
                        },
                        dismissContent = {
                            QueueListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(background.value)
                                    .shadow(elevation.value),
                                song = item,
                                index = i,
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
            queue = Song.dummies(5).toImmutableList(),
            index = 2,
            state = rememberReorderableLazyListState(onMove = { _, _ -> }),
            onSkipToQueue = { },
            onDeleteItem = { },
            onClickSongMenu = { },
        )
    }
}
