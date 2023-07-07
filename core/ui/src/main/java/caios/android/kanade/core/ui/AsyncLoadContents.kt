package caios.android.kanade.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.ui.view.ErrorView
import caios.android.kanade.core.ui.view.LoadingView

@Composable
fun <T> AsyncLoadContents(
    screenState: ScreenState<T>,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    retryAction: () -> Unit = {},
    content: @Composable (T?) -> Unit,
) {
    Box(Modifier.background(containerColor)) {
        content.invoke((screenState as? ScreenState.Idle<T>)?.data)

        AnimatedVisibility(
            modifier = modifier.align(Alignment.Center),
            visible = screenState is ScreenState.Loading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            LoadingView(
                modifier = Modifier.background(Color.Black.copy(alpha = 0.2f)),
            )
        }

        AnimatedVisibility(
            modifier = modifier.align(Alignment.Center),
            visible = screenState is ScreenState.Error,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            ErrorView(
                errorState = screenState as ScreenState.Error,
                retryAction = retryAction,
            )
        }
    }
}

@Composable
fun <T> FullAsyncLoadContents(
    screenState: ScreenState<T>,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable (T?) -> Unit,
) {
    Box(modifier.background(containerColor)) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = screenState is ScreenState.Idle,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            content.invoke((screenState as? ScreenState.Idle<T>)?.data)
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = screenState is ScreenState.Loading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            LoadingView(
                modifier = Modifier.background(Color.Black.copy(alpha = 0.2f)),
            )
        }
    }
}
