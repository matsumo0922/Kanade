package caios.android.kanade.feature.queue

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.dialog.showAsButtonSheet
import caios.android.kanade.feature.queue.items.QueueCurrentItemSection
import caios.android.kanade.feature.queue.items.QueueHeaderSection
import caios.android.kanade.feature.queue.items.QueueListSection
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import timber.log.Timber

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
    var data = remember { mutableStateListOf(*uiState.queue.toTypedArray()) }
    val scope = rememberCoroutineScope()
    val state = rememberReorderableLazyListState(
        onMove = { from, to -> data = data.apply { add(from.index, removeAt(to.index)) } },
        onDragEnd = { fromIndex, toIndex -> onMoveQueue.invoke(fromIndex, toIndex) },
    )

    Timber.d(data.size.toString())

    Column(modifier) {
        QueueHeaderSection(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth(),
            onClickDismiss = onClickDismiss,
            onClickMenuAddPlaylist = { onClickMenuAddPlaylist.invoke(uiState.queue) },
            onClickMenuShare = { onClickMenuShare.invoke(uiState.queue) },
        )

        QueueCurrentItemSection(
            modifier = Modifier.fillMaxWidth(),
            song = uiState.queue[uiState.index],
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
            index = uiState.index,
            state = state,
            onClickSongMenu = { onClickSongMenu.invoke(it) },
            onDeleteItem = onDeleteItem,
            onSkipToQueue = onClickSkipToQueue,
        )
    }
}

fun Activity.showQueueDialog(
    userData: UserData?,
    navigateToSongMenu: (Song) -> Unit,
) {
    showAsButtonSheet(userData) { onDismiss ->
        val viewModel = hiltViewModel<QueueViewModel>()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        AsyncLoadContents(
            modifier = Modifier.fillMaxSize(),
            screenState = screenState,
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
    QueueDialog(
        modifier = Modifier.fillMaxSize(),
        uiState = QueueUiState(
            queue = Song.dummies(10),
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
