package caios.android.kanade.feature.menu.playlist

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.core.ui.dialog.showAsButtonSheet
import caios.android.kanade.feature.menu.MenuItemSection

@Composable
fun PlaylistMenuDialog(
    playlist: Playlist,
    onClickPlayNext: () -> Unit,
    onClickPlayOnly: () -> Unit,
    onClickAddToQueue: () -> Unit,
    onClickRename: (Playlist) -> Unit,
    onClickShare: (Playlist) -> Unit,
    onClickDelete: (Playlist) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
    ) {
        PlaylistMenuHeader(
            modifier = Modifier.fillMaxWidth(),
            playlist = playlist,
        )

        Divider()

        MenuItemSection(
            titleRes = R.string.menu_play_next,
            imageVector = Icons.Default.DoubleArrow,
            onClick = {
                ToastUtil.show(context, R.string.menu_toast_add_to_queue)
                onDismiss.invoke()
                onClickPlayNext.invoke()
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_play_only_playlist,
            imageVector = Icons.Default.NavigateNext,
            onClick = {
                ToastUtil.show(context, R.string.menu_toast_add_to_queue)
                onDismiss.invoke()
                onClickPlayOnly.invoke()
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_add_to_queue,
            imageVector = Icons.Default.LibraryAdd,
            onClick = {
                ToastUtil.show(context, R.string.menu_toast_add_to_queue)
                onDismiss.invoke()
                onClickAddToQueue.invoke()
            },
        )

        Divider(
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 54.dp,
                ),
        )

        MenuItemSection(
            titleRes = R.string.menu_rename,
            imageVector = Icons.Default.Share,
            onClick = {
                onDismiss.invoke()
                onClickRename.invoke(playlist)
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_share,
            imageVector = Icons.Default.Share,
            onClick = {
                onDismiss.invoke()
                onClickShare.invoke(playlist)
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_delete_playlist,
            imageVector = Icons.Default.Delete,
            onClick = {
                onDismiss.invoke()
                onClickDelete.invoke(playlist)
            },
        )
    }
}

fun Activity.showPlaylistMenuDialog(
    musicViewModel: MusicViewModel,
    userData: UserData?,
    playlist: Playlist,
) {
    showAsButtonSheet(userData) { onDismiss ->
        PlaylistMenuDialog(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            playlist = playlist,
            onClickPlayNext = {
                musicViewModel.addToQueue(
                    songs = playlist.songs,
                    index = musicViewModel.uiState.queueIndex,
                )
            },
            onClickPlayOnly = {
                musicViewModel.playerEvent(
                    PlayerEvent.NewPlay(
                        index = 0,
                        queue = playlist.songs,
                        playWhenReady = true,
                    ),
                )
            },
            onClickAddToQueue = {
                musicViewModel.addToQueue(playlist.songs)
            },
            onClickRename = {},
            onClickShare = {},
            onClickDelete = {},
            onDismiss = onDismiss,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaylistMenuDialogPreview() {
    KanadeBackground {
        PlaylistMenuDialog(
            modifier = Modifier.fillMaxWidth(),
            playlist = Playlist.dummy(),
            onClickPlayNext = {},
            onClickPlayOnly = {},
            onClickAddToQueue = {},
            onClickRename = {},
            onClickShare = {},
            onClickDelete = {},
            onDismiss = {},
        )
    }
}
