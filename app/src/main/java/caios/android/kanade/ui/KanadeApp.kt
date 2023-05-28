package caios.android.kanade.ui

import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import caios.android.kanade.core.design.component.KanadeBackground

@Suppress("ModifierMissing")
@Composable
fun KanadeApp(
    windowSize: WindowSizeClass,
    appState: KanadeAppState = rememberKanadeAppState(windowSize),
) {
    KanadeBackground {
        Text(
            text = "Hello World!",
            color = Color.White,
        )
    }
}
