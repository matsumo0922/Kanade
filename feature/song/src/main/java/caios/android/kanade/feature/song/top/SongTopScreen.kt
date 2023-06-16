package caios.android.kanade.feature.song.top

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.AsyncLoadContents

@Composable
internal fun SongTopRoute(
    topMargin: Dp,
    modifier: Modifier = Modifier,
    viewModel: SongTopViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(screenState) {
        SongTopScreen(
            modifier = modifier,
            songs = it ?: emptyList(),
            contentPadding = PaddingValues(top = topMargin),
            onClickSong = viewModel::onNewPlay,
        )
    }
}

@Composable
internal fun SongTopScreen(
    songs: List<Song>,
    onClickSong: (Int, List<Song>) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        itemsIndexed(
            items = songs,
            key = { _, song -> song.id },
        ) { index, song ->
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clickable { onClickSong.invoke(index, songs) },
                text = song.title,
                color = Color.Gray,
            )
        }
    }
}
