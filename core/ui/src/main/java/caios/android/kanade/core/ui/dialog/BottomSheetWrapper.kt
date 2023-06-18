package caios.android.kanade.core.ui.dialog

import android.app.Activity
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheetWrapper(
    parent: ViewGroup,
    composeView: ComposeView,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    var isSheetOpened by remember { mutableStateOf(false) }

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            content {
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                }
            }
        },
    ) {}

    BackHandler {
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }
    }

    LaunchedEffect(modalBottomSheetState.currentValue) {
        if (modalBottomSheetState.currentValue == ModalBottomSheetValue.Hidden) {
            if (isSheetOpened) {
                parent.removeView(composeView)
            } else {
                launch {
                    delay(100)
                    modalBottomSheetState.show()
                    isSheetOpened = true
                }
            }
        }
    }
}

fun Activity.showAsButtonSheet(
    userData: UserData?,
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
                    BottomSheetWrapper(viewGroup, this, content = content)
                }
            }
        }
    }

    viewGroup.addView(view)
}
