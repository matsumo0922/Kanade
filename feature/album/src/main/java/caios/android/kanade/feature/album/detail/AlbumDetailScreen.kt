package caios.android.kanade.feature.album.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.SongHolder
import caios.android.kanade.core.ui.view.CoordinatorScaffold

@Composable
internal fun AlbumDetailRoute(
    albumId: Long,
    navigateToAlbumMenu: (Album) -> Unit,
    navigateToSongMenu: (Song) -> Unit,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlbumDetailViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(albumId) {
        viewModel.fetch(albumId)
    }

    AsyncLoadContents(screenState) {
        if (it != null) {
            AlbumDetailScreen(
                modifier = modifier,
                album = it.album,
                onClickSongHolder = viewModel::onNewPlay,
                onClickSongMenu = navigateToSongMenu,
                onClickMenu = navigateToAlbumMenu,
                onTerminate = terminate,
            )
        }
    }
}

@Composable
private fun AlbumDetailScreen(
    album: Album,
    onClickSongHolder: (List<Song>, Int) -> Unit,
    onClickSongMenu: (Song) -> Unit,
    onClickMenu: (Album) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CoordinatorScaffold(
        modifier = modifier,
        title = album.album,
        summary = album.artist,
        artwork = album.artwork,
        onClickNavigateUp = onTerminate,
        onClickMenu = { onClickMenu.invoke(album) },
    ) {
        itemsIndexed(
            items = album.songs,
            key = { _, song -> song.id },
        ) { index, song ->
            SongHolder(
                modifier = Modifier.fillMaxWidth(),
                song = song,
                onClickHolder = { onClickSongHolder.invoke(album.songs, index) },
                onClickMenu = { onClickSongMenu.invoke(song) },
            )
        }
    }
}
