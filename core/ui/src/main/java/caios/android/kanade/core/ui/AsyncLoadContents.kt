@file:Suppress("ModifierReused")

package caios.android.kanade.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.ui.view.ErrorView
import caios.android.kanade.core.ui.view.LoadingView

@Composable
fun <T> AsyncLoadContents(
    screenState: ScreenState<T>,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    cornerShape: RoundedCornerShape = RoundedCornerShape(0.dp),
    retryAction: () -> Unit = {},
    content: @Composable (T) -> Unit,
) {
    AnimatedContent(
        modifier = modifier
            .clip(cornerShape)
            .background(containerColor),
        targetState = screenState,
        transitionSpec = { fadeIn().togetherWith(fadeOut()) },
        contentKey = { it.javaClass },
        label = "AsyncLoadContents",
    ) { state ->
        when (state) {
            is ScreenState.Idle -> {
                content.invoke(state.data)
            }
            is ScreenState.Loading -> {
                LoadingView(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f)),
                )
            }
            is ScreenState.Error -> {
                ErrorView(
                    modifier = Modifier.fillMaxSize(),
                    errorState = state,
                    retryAction = retryAction,
                )
            }
        }
    }
}

@Composable
fun <T> AsyncNoLoadContents(
    screenState: ScreenState<T>,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    cornerShape: RoundedCornerShape = RoundedCornerShape(0.dp),
    retryAction: () -> Unit = {},
    content: @Composable (T?) -> Unit,
) {
    AnimatedContent(
        modifier = modifier
            .clip(cornerShape)
            .background(containerColor),
        targetState = screenState,
        transitionSpec = { fadeIn().togetherWith(fadeOut()) },
        contentKey = { it.javaClass },
        label = "AsyncNoLoadContents",
    ) { state ->
        when (state) {
            is ScreenState.Idle, is ScreenState.Loading -> {
                content.invoke((state as? ScreenState.Idle)?.data)
            }
            is ScreenState.Error -> {
                ErrorView(
                    modifier = Modifier.fillMaxSize(),
                    errorState = state,
                    retryAction = retryAction,
                )
            }
        }
    }
}
