package caios.android.kanade.feature.album.top

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
import caios.android.kanade.core.model.Album
import caios.android.kanade.core.ui.AsyncLoadContents

@Composable
internal fun AlbumTopRoute(
    topMargin: Dp,
    modifier: Modifier = Modifier,
    viewModel: AlbumTopViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(screenState) {
        AlbumTopScreen(
            albums = it ?: emptyList(),
            modifier = modifier,
            contentPadding = PaddingValues(top = topMargin),
        )
    }
}

@Composable
internal fun AlbumTopScreen(
    albums: List<Album>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        items(
            items = albums,
            key = { it.albumId },
        ) {
            Text(
                text = it.album,
                color = Color.Gray,
            )
        }
    }
}
