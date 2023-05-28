package caios.android.kanade.feature.playlist.top

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun PlaylistTopRoute(
    modifier: Modifier = Modifier,
    viewModel: PlaylistTopViewModel = hiltViewModel(),
) {
    PlaylistTopScreen()
}

@Composable
internal fun PlaylistTopScreen() {
    Text(
        text = "AlbumTopScreen",
        color = Color.Gray,
    )
}
