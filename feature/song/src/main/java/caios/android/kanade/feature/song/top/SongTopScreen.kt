package caios.android.kanade.feature.song.top

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun SongTopRoute(
    topMargin: Dp,
    modifier: Modifier = Modifier,
    viewModel: SongTopViewModel = hiltViewModel(),
) {
    SongTopScreen(
        modifier = modifier,
        contentPadding = PaddingValues(top = topMargin),
    )
}

@Composable
internal fun SongTopScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(100) {
            Text(
                text = "SongTopScreen $it",
                color = Color.Gray,
            )
        }
    }
}
