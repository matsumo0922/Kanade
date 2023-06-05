package caios.android.kanade.core.ui.controller

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun AppController(
    offsetRate: Float,
    onClickBottomController: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        if (offsetRate > 0f) {
            BottomController(
                modifier = Modifier
                    .alpha(offsetRate)
                    .height(72.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onClickBottomController() },
                onClickPlay = {},
                onClickPause = {},
                onClickSkipToNext = {},
                onClickSkipToPrevious = {},
            )
        }

        if (offsetRate < 1f) {
            MainController(
                modifier = Modifier
                    .alpha(1f - offsetRate)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            )
        }
    }
}
