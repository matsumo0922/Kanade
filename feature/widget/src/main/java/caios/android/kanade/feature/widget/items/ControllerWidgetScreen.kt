package caios.android.kanade.feature.widget.items

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.text.Text

@Composable
internal fun ControllerWidgetScreen(
    modifier: GlanceModifier = GlanceModifier,
) {
    Text(
        modifier = modifier,
        text = "Hello, World!",
    )
}
