package caios.android.kanade.feature.album.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.SongHolder
import caios.android.kanade.core.ui.view.CoordinatorData
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

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
        retryAction = { terminate.invoke() },
    ) {
        AlbumDetailScreen(
            modifier = Modifier.fillMaxSize(),
            album = it.album,
            onClickSongHolder = viewModel::onNewPlay,
            onClickSongMenu = navigateToSongMenu,
            onClickMenu = navigateToAlbumMenu,
            onClickShuffle = viewModel::onShufflePlay,
            onTerminate = terminate,
        )
    }
}

@Composable
private fun AlbumDetailScreen(
    album: Album,
    onClickSongHolder: (List<Song>, Int) -> Unit,
    onClickSongMenu: (Song) -> Unit,
    onClickMenu: (Album) -> Unit,
    onClickShuffle: (List<Song>) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isVisibleFAB by remember { mutableStateOf(false) }
    val coordinatorData = remember { CoordinatorData.Album(album.album, album.artist, album.artwork) }

    LaunchedEffect(album) {
        isVisibleFAB = true
    }

    Box(modifier) {
        CoordinatorScaffold(
            modifier = Modifier.fillMaxSize(),
            data = coordinatorData,
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

            item {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp),
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            visible = isVisibleFAB,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onClickShuffle.invoke(album.songs) },
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = null,
                )
            }
        }
    }
}
