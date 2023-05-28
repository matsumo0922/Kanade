package caios.android.kanade.feature.artist.top

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun ArtistTopRoute(
    modifier: Modifier = Modifier,
    viewModel: ArtistTopViewModel = hiltViewModel(),
) {
    ArtistTopScreen()
}

@Composable
internal fun ArtistTopScreen() {
    Text(
        text = "ArtistTopScreen",
        color = Color.Gray,
    )
}
