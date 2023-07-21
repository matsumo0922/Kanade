package caios.android.kanade.feature.song.top

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.SongHolder
import caios.android.kanade.core.ui.music.SortInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.reflect.KClass

@Composable
internal fun SongTopRoute(
    topMargin: Dp,
    navigateToSongMenu: (Song) -> Unit,
    navigateToSort: (KClass<*>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SongTopViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
    ) { uiState ->
        SongTopScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            songs = uiState?.songs?.toImmutableList() ?: persistentListOf(),
            sortOrder = uiState?.sortOrder ?: MusicOrder.songDefault(),
            contentPadding = PaddingValues(top = topMargin + 8.dp),
            onClickSort = { navigateToSort.invoke(MusicOrderOption.Song::class) },
            onClickSong = viewModel::onNewPlay,
            onClickMenu = navigateToSongMenu,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SongTopScreen(
    songs: ImmutableList<Song>,
    sortOrder: MusicOrder,
    onClickSort: (MusicOrder) -> Unit,
    onClickSong: (Int, List<Song>) -> Unit,
    onClickMenu: (Song) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        item {
            SortInfo(
                sortOrder = sortOrder,
                itemSize = songs.size,
                onClickSort = onClickSort,
            )
        }

        itemsIndexed(
            items = songs,
            key = { _, song -> song.id },
        ) { index, song ->
            SongHolder(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
                song = song,
                onClickHolder = { onClickSong.invoke(index, songs) },
                onClickMenu = { onClickMenu.invoke(song) },
            )
        }
    }
}
