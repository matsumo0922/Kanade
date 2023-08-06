package caios.android.kanade.feature.menu.album

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
import androidx.compose.material.icons.filled.PlaylistAdd
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
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.core.ui.dialog.showAsButtonSheet
import caios.android.kanade.feature.menu.MenuItemSection

@Composable
fun AlbumMenuDialog(
    album: Album,
    onClickPlayNext: () -> Unit,
    onClickPlayOnly: () -> Unit,
    onClickAddToQueue: () -> Unit,
    onClickAddToPlaylist: (Album) -> Unit,
    onClickShare: (Album) -> Unit,
    onClickDelete: (Album) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
    ) {
        AlbumMenuHeader(
            modifier = Modifier.fillMaxWidth(),
            album = album,
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
            titleRes = R.string.menu_play_only_album,
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

        MenuItemSection(
            titleRes = R.string.menu_add_to_playlist,
            imageVector = Icons.Default.PlaylistAdd,
            onClick = {
                onDismiss.invoke()
                onClickAddToPlaylist.invoke(album)
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
            titleRes = R.string.menu_share,
            imageVector = Icons.Default.Share,
            onClick = {
                onDismiss.invoke()
                onClickShare.invoke(album)
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_delete,
            imageVector = Icons.Default.Delete,
            onClick = {
                onDismiss.invoke()
                onClickDelete.invoke(album)
            },
        )
    }
}

fun Activity.showAlbumMenuDialog(
    musicViewModel: MusicViewModel,
    userData: UserData?,
    album: Album,
    navigateToAddToPlaylist: (List<Long>) -> Unit,
    navigateToShare: (Album) -> Unit,
    navigateToDelete: (Album) -> Unit,
) {
    showAsButtonSheet(userData) { onDismiss ->
        AlbumMenuDialog(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            album = album,
            onClickPlayNext = {
                musicViewModel.addToQueue(
                    songs = album.songs,
                    index = musicViewModel.uiState.queueIndex + 1,
                )
            },
            onClickPlayOnly = {
                musicViewModel.playerEvent(
                    PlayerEvent.NewPlay(
                        index = 0,
                        queue = album.songs,
                        playWhenReady = true,
                    ),
                )
            },
            onClickAddToQueue = {
                musicViewModel.addToQueue(album.songs)
            },
            onClickAddToPlaylist = { album ->
                navigateToAddToPlaylist.invoke(album.songs.map { it.id })
            },
            onClickShare = navigateToShare,
            onClickDelete = navigateToDelete,
            onDismiss = onDismiss,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlbumMenuDialogPreview() {
    KanadeBackground {
        AlbumMenuDialog(
            modifier = Modifier.fillMaxWidth(),
            album = Album.dummy(),
            onClickPlayNext = {},
            onClickPlayOnly = {},
            onClickAddToQueue = {},
            onClickAddToPlaylist = {},
            onClickShare = {},
            onClickDelete = {},
            onDismiss = {},
        )
    }
}
