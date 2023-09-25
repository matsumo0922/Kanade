package caios.android.kanade.feature.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.fillMaxWidth
import caios.android.kanade.feature.widget.items.ControllerWidgetScreen

class ControllerWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                ControllerWidgetScreen(
                    modifier = GlanceModifier.fillMaxWidth(),
                )
            }
        }
    }
}
