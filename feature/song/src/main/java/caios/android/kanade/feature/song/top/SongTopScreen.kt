package caios.android.kanade.feature.song.top

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
            songs = it ?: emptyList(),
            modifier = modifier,
            contentPadding = PaddingValues(top = topMargin),
        )
    }

    /*Box(Modifier.fillMaxSize()) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { viewModel.fetchSongs() }
        ) {
            Text(text = "Button")
        }
    }*/
}

@Composable
internal fun SongTopScreen(
    songs: List<Song>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        items(
            items = songs,
            key = { it.id },
        ) {
            Text(
                text = it.title,
                color = Color.Gray,
            )
        }
    }
}
