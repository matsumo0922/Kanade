package caios.android.kanade.feature.artist.top

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun ArtistTopRoute(
    topMargin: Dp,
    modifier: Modifier = Modifier,
    viewModel: ArtistTopViewModel = hiltViewModel(),
) {
    ArtistTopScreen(
        modifier = modifier,
        contentPadding = PaddingValues(top = topMargin),
    )
}

@Composable
internal fun ArtistTopScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Text(
        text = "ArtistTopScreen",
        color = Color.Gray,
    )
}
