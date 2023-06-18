package caios.android.kanade.feature.playlist.top

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun PlaylistTopRoute(
    topMargin: Dp,
    modifier: Modifier = Modifier,
) {
    PlaylistTopScreen(
        modifier = modifier,
        contentPadding = PaddingValues(top = topMargin),
    )
}

@Composable
internal fun PlaylistTopScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(modifier.padding(contentPadding)) {
        Text(
            text = "AlbumTopScreen",
            color = Color.Gray,
        )
    }
}
