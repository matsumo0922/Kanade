package caios.android.kanade.core.ui.dialog

import android.app.Activity
import android.view.ViewGroup
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.KanadeTheme
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetWrapper(
    parent: ViewGroup,
    composeView: ComposeView,
    skipPartiallyExpanded: Boolean,
    willFullScreen: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState(skipPartiallyExpanded)
    var isOpen by rememberSaveable { mutableStateOf(true) }
    val cornerRadius = animateDpAsState(
        targetValue = if (state.targetValue != SheetValue.Hidden && !willFullScreen) 16.dp else 0.dp,
        label = "cornerRadius",
    )

    LaunchedEffect(isOpen) {
        if (!isOpen) {
            delay(100)
            parent.removeView(composeView)
        }
    }

    if (isOpen) {
        ModalBottomSheet(
            modifier = modifier,
            sheetState = state,
            dragHandle = { },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(
                topStart = cornerRadius.value,
                topEnd = cornerRadius.value,
            ),
            windowInsets = WindowInsets(0, 0, 0, 0),
            onDismissRequest = { isOpen = false },
        ) {
            content {
                scope.launch {
                    state.hide()
                }
            }
        }
    }
}

fun Activity.showAsButtonSheet(
    userData: UserData?,
    skipPartiallyExpanded: Boolean = true,
    willFullScreen: Boolean = false,
    content: @Composable (() -> Unit) -> Unit,
) {
    val viewGroup = findViewById<ViewGroup>(android.R.id.content)
    val view = ComposeView(viewGroup.context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            val shouldUseDarkTheme = when (userData?.themeConfig) {
                ThemeConfig.Light -> false
                ThemeConfig.Dark -> true
                else -> isSystemInDarkTheme()
            }

            KanadeTheme(
                shouldUseDarkTheme = shouldUseDarkTheme,
                enableDynamicTheme = userData?.useDynamicColor ?: false,
            ) {
                KanadeBackground(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.Transparent,
                ) {
                    BottomSheetWrapper(
                        parent = viewGroup,
                        composeView = this,
                        skipPartiallyExpanded = skipPartiallyExpanded,
                        willFullScreen = willFullScreen,
                        content = content,
                    )
                }
            }
        }
    }

    viewGroup.addView(view)
}
