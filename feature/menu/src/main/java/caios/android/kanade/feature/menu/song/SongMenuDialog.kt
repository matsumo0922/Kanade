package caios.android.kanade.feature.menu.song

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.Lyrics
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.dialog.showAsButtonSheet
import caios.android.kanade.feature.menu.MenuItemSection

@Composable
private fun SongMenuDialog(
    song: Song,
    onClickFavorite: (Boolean) -> Unit,
    onClickPlayNext: () -> Unit,
    onClickPlayOnly: () -> Unit,
    onClickAddToQueue: () -> Unit,
    onClickAddToPlaylist: (Song) -> Unit,
    onClickArtist: (Song) -> Unit,
    onClickAlbum: (Song) -> Unit,
    onClickAnalyzeMusicInfo: (Song) -> Unit,
    onClickEditMusicInfo: (Song) -> Unit,
    onClickLyrics: (Song) -> Unit,
    onClickMusicDetailInfo: (Song) -> Unit,
    onClickShare: (Song) -> Unit,
    onClickDelete: (Song) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        SongMenuHeader(
            modifier = Modifier.fillMaxWidth(),
            song = song,
            isFavorite = false,
            onClickFavorite = onClickFavorite,
        )

        Divider()

        MenuItemSection(
            titleRes = R.string.menu_play_next,
            imageVector = Icons.Default.DoubleArrow,
            onClick = onClickPlayNext,
        )

        MenuItemSection(
            titleRes = R.string.menu_play_only,
            imageVector = Icons.Default.NavigateNext,
            onClick = onClickPlayOnly,
        )

        MenuItemSection(
            titleRes = R.string.menu_add_to_queue,
            imageVector = Icons.Default.LibraryAdd,
            onClick = onClickAddToQueue,
        )

        MenuItemSection(
            titleRes = R.string.menu_add_to_playlist,
            imageVector = Icons.Default.PlaylistAdd,
            onClick = {
                onDismiss.invoke()
                onClickAddToPlaylist.invoke(song)
            },
        )

        Divider(
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 54.dp
                ),
        )

        MenuItemSection(
            titleRes = R.string.menu_artist,
            imageVector = Icons.Default.Person,
            onClick = {
                onDismiss.invoke()
                onClickArtist.invoke(song)
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_album,
            imageVector = Icons.Default.Album,
            onClick = {
                onDismiss.invoke()
                onClickAlbum.invoke(song)
            },
        )

        Divider(
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 54.dp
                ),
        )

        MenuItemSection(
            titleRes = R.string.menu_analyze,
            imageVector = Icons.Default.Code,
            onClick = {
                onDismiss.invoke()
                onClickAnalyzeMusicInfo.invoke(song)
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_edit,
            imageVector = Icons.Default.Edit,
            onClick = {
                onDismiss.invoke()
                onClickEditMusicInfo.invoke(song)
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_lyrics,
            imageVector = Icons.Default.Lyrics,
            onClick = {
                onDismiss.invoke()
                onClickLyrics.invoke(song)
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_detail_info,
            imageVector = Icons.Default.Info,
            onClick = {
                onDismiss.invoke()
                onClickMusicDetailInfo.invoke(song)
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_share,
            imageVector = Icons.Default.Share,
            onClick = {
                onDismiss.invoke()
                onClickShare.invoke(song)
            },
        )

        MenuItemSection(
            titleRes = R.string.menu_delete,
            imageVector = Icons.Default.Delete,
            onClick = {
                onDismiss.invoke()
                onClickDelete.invoke(song)
            },
        )
    }
}

fun Activity.showSongMenuDialog(userData: UserData?, song: Song) {
    showAsButtonSheet(userData) {
        SongMenuDialog(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            song = song,
            onClickFavorite = { /*TODO*/},
            onClickPlayNext = { /*TODO*/ },
            onClickPlayOnly = { /*TODO*/ },
            onClickAddToQueue = { /*TODO*/ },
            onClickAddToPlaylist = { /*TODO*/},
            onClickArtist = { /*TODO*/},
            onClickAlbum = { /*TODO*/},
            onClickAnalyzeMusicInfo = { /*TODO*/},
            onClickEditMusicInfo = { /*TODO*/},
            onClickLyrics = { /*TODO*/},
            onClickMusicDetailInfo = { /*TODO*/},
            onClickShare = { /*TODO*/},
            onClickDelete = { /*TODO*/},
            onDismiss = { /*TODO*/ },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SongMenuDialogPreview() {
    KanadeBackground {
        SongMenuDialog(
            modifier = Modifier.fillMaxWidth(),
            song = Song.dummy(),
            onClickFavorite = {},
            onClickPlayNext = {},
            onClickPlayOnly = {},
            onClickAddToQueue = {},
            onClickAddToPlaylist = {},
            onClickArtist = {},
            onClickAlbum = {},
            onClickAnalyzeMusicInfo = {},
            onClickEditMusicInfo = {},
            onClickLyrics = {},
            onClickMusicDetailInfo = {},
            onClickShare = {},
            onClickDelete = {},
            onDismiss = {},
        )
    }
}