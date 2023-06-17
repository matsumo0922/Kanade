package caios.android.kanade.feature.song.top

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
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.MusicHolder
import caios.android.kanade.core.ui.music.SortInfo
import caios.android.kanade.feature.song.top.items.SongTopHeaderSection

@Composable
internal fun SongTopRoute(
    topMargin: Dp,
    modifier: Modifier = Modifier,
    viewModel: SongTopViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(screenState) { uiState ->
        SongTopScreen(
            modifier = modifier.background(MaterialTheme.colorScheme.surface),
            songs = uiState?.songs ?: emptyList(),
            sortOrder = uiState?.sortOrder ?: MusicOrder.songDefault(),
            contentPadding = PaddingValues(top = topMargin),
            onClickSort = { /*TODO*/ },
            onClickSong = viewModel::onNewPlay,
            onClickMenu = { /*TODO*/ },
            onClickHistory = { /*TODO*/ },
            onClickRecentlyAdd = { /*TODO*/ },
            onClickMostPlayed = { /*TODO*/ },
            onClickShuffle = viewModel::onShuffle,
        )
    }
}

@Composable
internal fun SongTopScreen(
    songs: List<Song>,
    sortOrder: MusicOrder,
    onClickSort: (MusicOrder) -> Unit,
    onClickSong: (Int, List<Song>) -> Unit,
    onClickMenu: (Song) -> Unit,
    onClickHistory: () -> Unit,
    onClickRecentlyAdd: () -> Unit,
    onClickMostPlayed: () -> Unit,
    onClickShuffle: (List<Song>) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        item {
            SongTopHeaderSection(
                modifier = Modifier.fillMaxWidth(),
                onClickHistory = onClickHistory,
                onClickRecentlyAdd = onClickRecentlyAdd,
                onClickMostPlayed = onClickMostPlayed,
                onClickShuffle = { onClickShuffle.invoke(songs) },
            )
        }

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
            MusicHolder(
                modifier = Modifier.fillMaxWidth(),
                song = song,
                onClickHolder = { onClickSong.invoke(index, songs) },
                onClickMenu = { onClickMenu.invoke(song) },
            )
        }
    }
}
