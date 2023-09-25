package caios.android.kanade.feature.album.top

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.AlbumHolder
import caios.android.kanade.core.ui.music.SortInfo
import caios.android.kanade.core.ui.view.FixedWithEdgeSpace
import caios.android.kanade.core.ui.view.itemsWithEdgeSpace
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.reflect.KClass

@Composable
internal fun AlbumTopRoute(
    topMargin: Dp,
    navigateToAlbumDetail: (Long) -> Unit,
    navigateToAlbumMenu: (Album) -> Unit,
    navigateToSort: (KClass<*>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlbumTopViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
    ) { uiState ->
        AlbumTopScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            albums = uiState.albums.toImmutableList(),
            sortOrder = uiState.sortOrder,
            onClickSort = { navigateToSort.invoke(MusicOrderOption.Album::class) },
            onClickAlbum = navigateToAlbumDetail,
            onClickPlay = viewModel::onNewPlay,
            onClickMenu = navigateToAlbumMenu,
            contentPadding = PaddingValues(top = topMargin + 8.dp, bottom = 8.dp),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AlbumTopScreen(
    albums: ImmutableList<Album>,
    sortOrder: MusicOrder,
    onClickSort: (MusicOrder) -> Unit,
    onClickAlbum: (Long) -> Unit,
    onClickPlay: (Album) -> Unit,
    onClickMenu: (Album) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = contentPadding,
        columns = FixedWithEdgeSpace(
            count = 2,
            edgeSpace = 8.dp,
        ),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            SortInfo(
                sortOrder = sortOrder,
                itemSize = albums.size,
                onClickSort = onClickSort,
            )
        }

        itemsWithEdgeSpace(
            spanCount = 2,
            items = albums,
            key = { album -> album.albumId },
        ) { album ->
            AlbumHolder(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
                album = album,
                onClickHolder = { onClickAlbum.invoke(album.albumId) },
                onClickPlay = { onClickPlay.invoke(album) },
                onClickMenu = { onClickMenu.invoke(album) },
            )
        }
    }
}
