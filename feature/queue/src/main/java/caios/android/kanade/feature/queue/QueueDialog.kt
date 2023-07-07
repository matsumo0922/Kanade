package caios.android.kanade.feature.queue

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.QueueItem
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.dialog.showAsButtonSheet
import caios.android.kanade.feature.queue.items.QueueCurrentItemSection
import caios.android.kanade.feature.queue.items.QueueHeaderSection
import caios.android.kanade.feature.queue.items.QueueListSection
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.rememberReorderableLazyListState

@Composable
private fun QueueDialog(
    uiState: QueueUiState,
    onClickDismiss: () -> Unit,
    onClickMenuAddPlaylist: (List<Song>) -> Unit,
    onClickMenuShare: (List<Song>) -> Unit,
    onClickSongMenu: (Song) -> Unit,
    onClickSkipToQueue: (Int) -> Unit,
    onDeleteItem: (Int) -> Unit,
    onMoveQueue: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var index by remember { mutableIntStateOf(uiState.index) }
    var data = remember { mutableStateListOf(*uiState.queue.toTypedArray()) }
    val scope = rememberCoroutineScope()
    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            val currentSong = data[index]
            data = data.apply { add(to.index, removeAt(from.index)) }
            index = data.indexOf(currentSong)
        },
        onDragEnd = { fromIndex, toIndex -> onMoveQueue.invoke(fromIndex, toIndex) },
    )

    Column(modifier) {
        QueueHeaderSection(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth(),
            onClickDismiss = onClickDismiss,
            onClickMenuAddPlaylist = { onClickMenuAddPlaylist.invoke(uiState.queue.map { it.song }) },
            onClickMenuShare = { onClickMenuShare.invoke(uiState.queue.map { it.song }) },
        )

        QueueCurrentItemSection(
            modifier = Modifier.fillMaxWidth(),
            song = uiState.queue[uiState.index].song,
            isPlaying = uiState.isPlaying,
            onClickHolder = {
                scope.launch {
                    state.listState.animateScrollToItem(uiState.index)
                }
            },
        )

        QueueListSection(
            modifier = Modifier.fillMaxWidth(),
            queue = data.toImmutableList(),
            index = index,
            state = state,
            onClickSongMenu = { onClickSongMenu.invoke(it) },
            onSkipToQueue = {
                index = it
                onClickSkipToQueue.invoke(it)
            },
            onDeleteItem = {
                val currentItem = data[index]
                val deleteIndex = data.indexOf(it)

                if (index == deleteIndex) return@QueueListSection false

                data = data.apply { removeAt(deleteIndex) }
                index = data.indexOf(currentItem)

                onDeleteItem.invoke(deleteIndex)

                return@QueueListSection true
            },
        )
    }
}

fun Activity.showQueueDialog(
    userData: UserData?,
    navigateToSongMenu: (Song) -> Unit,
) {
    showAsButtonSheet(userData, willFullScreen = true) { onDismiss ->
        val viewModel = hiltViewModel<QueueViewModel>()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        LaunchedEffect(screenState) {
            if (screenState is ScreenState.Error) {
                onDismiss.invoke()
            }
        }

        AsyncLoadContents(
            modifier = Modifier.fillMaxSize(),
            screenState = screenState,
            retryAction = { onDismiss.invoke() }
        ) {
            if (it != null) {
                QueueDialog(
                    modifier = Modifier.fillMaxSize(),
                    uiState = it,
                    onClickDismiss = onDismiss,
                    onClickMenuAddPlaylist = { },
                    onClickMenuShare = { },
                    onClickSongMenu = navigateToSongMenu,
                    onClickSkipToQueue = viewModel::onSkipToQueue,
                    onDeleteItem = viewModel::onDeleteItem,
                    onMoveQueue = viewModel::onQueueChanged,
                )
            }
        }
    }
}

@Preview
@Composable
private fun QueueDialogPreview() {
    KanadeBackground(backgroundColor = MaterialTheme.colorScheme.surface) {
        QueueDialog(
            modifier = Modifier.fillMaxSize(),
            uiState = QueueUiState(
                queue = Song.dummies(10).mapIndexed { index, song -> QueueItem(song, index) },
                index = 2,
                isPlaying = false,
            ),
            onClickDismiss = { },
            onClickMenuAddPlaylist = { },
            onClickMenuShare = { },
            onClickSongMenu = { },
            onClickSkipToQueue = { },
            onDeleteItem = { },
            onMoveQueue = { _, _ -> },
        )
    }
}
