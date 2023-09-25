package caios.android.kanade.feature.artist.top

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
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.ArtistHolder
import caios.android.kanade.core.ui.music.SortInfo
import caios.android.kanade.core.ui.view.FixedWithEdgeSpace
import caios.android.kanade.core.ui.view.itemsWithEdgeSpace
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.reflect.KClass

@Composable
internal fun ArtistTopRoute(
    topMargin: Dp,
    navigateToArtistDetail: (Long) -> Unit,
    navigateToSort: (KClass<*>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ArtistTopViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
    ) { uiState ->
        ArtistTopScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            artists = uiState.artists.toImmutableList(),
            sortOrder = uiState.sortOrder,
            onClickSort = { navigateToSort.invoke(MusicOrderOption.Artist::class) },
            onClickArtist = navigateToArtistDetail,
            contentPadding = PaddingValues(top = topMargin + 8.dp, bottom = 8.dp),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ArtistTopScreen(
    artists: ImmutableList<Artist>,
    sortOrder: MusicOrder,
    onClickSort: (MusicOrder) -> Unit,
    onClickArtist: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = contentPadding,
        columns = FixedWithEdgeSpace(
            count = 3,
            edgeSpace = 8.dp,
        ),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            SortInfo(
                sortOrder = sortOrder,
                itemSize = artists.size,
                onClickSort = onClickSort,
            )
        }

        itemsWithEdgeSpace(
            spanCount = 3,
            items = artists,
            key = { artist -> artist.artistId },
        ) { artist ->
            ArtistHolder(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
                artist = artist,
                onClickHolder = { onClickArtist.invoke(artist.artistId) },
            )
        }
    }
}
