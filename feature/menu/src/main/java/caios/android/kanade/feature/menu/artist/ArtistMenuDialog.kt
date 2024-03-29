package caios.android.kanade.feature.menu.artist

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import caios.android.kanade.core.design.theme.LocalSystemBars
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.core.ui.dialog.showAsButtonSheet
import caios.android.kanade.feature.menu.MenuItemSection

@Composable
fun ArtistMenuDialog(
    artist: Artist,
    onClickPlayNext: () -> Unit,
    onClickPlayOnly: () -> Unit,
    onClickAddToQueue: () -> Unit,
    onClickAddToPlaylist: (Artist) -> Unit,
    onClickShare: (Artist) -> Unit,
    onClickDelete: (Artist) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(modifier.verticalScroll(rememberScrollState())) {
        ArtistMenuHeader(
            modifier = Modifier.fillMaxWidth(),
            artist = artist,
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
            titleRes = R.string.menu_play_only_artist,
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
                onClickAddToPlaylist.invoke(artist)
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
                onClickShare.invoke(artist)
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_delete,
            imageVector = Icons.Default.Delete,
            onClick = {
                onDismiss.invoke()
                onClickDelete.invoke(artist)
            },
        )
    }
}

fun Activity.showArtistMenuDialog(
    musicViewModel: MusicViewModel,
    userData: UserData?,
    artist: Artist,
    navigateToAddToPlaylist: (List<Long>) -> Unit,
    navigateToShare: (Artist) -> Unit,
    navigateToDelete: (Artist) -> Unit,
) {
    showAsButtonSheet(userData) { onDismiss ->
        ArtistMenuDialog(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(bottom = LocalSystemBars.current.bottom),
            artist = artist,
            onClickPlayNext = {
                musicViewModel.addToQueue(
                    songs = artist.songs,
                    index = musicViewModel.uiState.queueIndex + 1,
                )
            },
            onClickPlayOnly = {
                musicViewModel.playerEvent(
                    PlayerEvent.NewPlay(
                        index = 0,
                        queue = artist.songs,
                        playWhenReady = true,
                    ),
                )
            },
            onClickAddToQueue = {
                musicViewModel.addToQueue(artist.songs)
            },
            onClickAddToPlaylist = { artist ->
                navigateToAddToPlaylist.invoke(artist.songs.map { it.id })
            },
            onClickShare = navigateToShare,
            onClickDelete = navigateToDelete,
            onDismiss = onDismiss,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtistMenuDialogPreview() {
    KanadeBackground {
        ArtistMenuDialog(
            modifier = Modifier.fillMaxWidth(),
            artist = Artist.dummy(),
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
