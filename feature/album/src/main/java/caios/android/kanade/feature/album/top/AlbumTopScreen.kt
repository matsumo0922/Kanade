package caios.android.kanade.feature.album.top

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun AlbumTopRoute(
    topMargin: Dp,
    modifier: Modifier = Modifier,
    viewModel: AlbumTopViewModel = hiltViewModel(),
) {
    AlbumTopScreen(
        modifier = modifier,
        contentPadding = PaddingValues(top = topMargin),
    )
}

@Composable
internal fun AlbumTopScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Text(
        text = "AlbumTopScreen",
        color = Color.Gray,
    )
}
