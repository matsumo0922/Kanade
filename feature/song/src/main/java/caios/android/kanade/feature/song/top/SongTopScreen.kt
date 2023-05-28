package caios.android.kanade.feature.song.top

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun SongTopRoute(
    modifier: Modifier = Modifier,
    viewModel: SongTopViewModel = hiltViewModel()
) {
    SongTopScreen()
}

@Composable
internal fun SongTopScreen() {
    Text(
        text = "SongTopScreen",
        color = Color.Gray,
    )
}