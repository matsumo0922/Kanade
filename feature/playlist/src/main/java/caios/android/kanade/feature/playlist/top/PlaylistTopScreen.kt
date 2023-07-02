package caios.android.kanade.feature.playlist.top

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.PlaylistHolder
import caios.android.kanade.core.ui.music.SortInfo
import caios.android.kanade.core.ui.view.FixedWithEdgeSpace
import caios.android.kanade.core.ui.view.itemsWithEdgeSpace
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun PlaylistTopRoute(
    topMargin: Dp,
    navigateToPlaylistMenu: (Playlist) -> Unit,
    navigateToPlaylistEdit: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaylistTopViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
    ) { uiState ->
        PlaylistTopScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            contentPadding = PaddingValues(top = topMargin),
            playlists = uiState?.playlists?.toImmutableList() ?: persistentListOf(),
            sortOrder = uiState?.sortOrder ?: MusicOrder.playlistDefault(),
            onClickSort = { },
            onClickEdit = navigateToPlaylistEdit,
            onClickPlaylist = { },
            onClickPlay = viewModel::onNewPlay,
            onClickMenu = navigateToPlaylistMenu,
        )
    }
}

@Composable
internal fun PlaylistTopScreen(
    playlists: List<Playlist>,
    sortOrder: MusicOrder,
    onClickSort: (MusicOrder) -> Unit,
    onClickEdit: () -> Unit,
    onClickPlaylist: (Long) -> Unit,
    onClickPlay: (Playlist) -> Unit,
    onClickMenu: (Playlist) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    var isVisibleFAB by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        isVisibleFAB = true
    }

    Box(modifier) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            columns = FixedWithEdgeSpace(
                count = 2,
                edgeSpace = 8.dp,
            ),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SortInfo(
                    sortOrder = sortOrder,
                    itemSize = playlists.size,
                    onClickSort = onClickSort,
                )
            }

            itemsWithEdgeSpace(
                spanCount = 2,
                items = playlists,
                key = { it.id },
            ) { playlist ->
                PlaylistHolder(
                    modifier = Modifier.fillMaxWidth(),
                    playlist = playlist,
                    onClickHolder = { onClickPlaylist.invoke(playlist.id) },
                    onClickPlay = { onClickPlay.invoke(playlist) },
                    onClickMenu = { onClickMenu.invoke(playlist) },
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .padding(contentPadding.calculateBottomPadding())
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            visible = isVisibleFAB,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onClickEdit.invoke() },
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
            }
        }
    }
}
