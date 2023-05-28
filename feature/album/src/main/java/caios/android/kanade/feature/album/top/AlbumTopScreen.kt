package caios.android.kanade.feature.album.top

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

@Composable
internal fun AlbumTopRoute(
    modifier: Modifier = Modifier,
    viewModel: AlbumTopViewModel = hiltViewModel()
) {
    AlbumTopScreen()
}

@Composable
internal fun AlbumTopScreen() {
    Text(
        text = "AlbumTopScreen",
        color = Color.Gray,
    )
}