@file:Suppress("MatchingDeclarationName")

package caios.android.kanade.core.ui.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.theme.SystemBars

fun Modifier.systemBarsPadding(systemBars: SystemBars) = this.padding(
    top = systemBars.top,
    bottom = systemBars.bottom,
)

fun Modifier.extraSize(width: Dp, height: Dp) = this.layout { measurable, constraints ->
    val placeable = measurable.measure(
        constraints.copy(
            maxWidth = constraints.maxWidth + width.roundToPx() * 2,
            maxHeight = constraints.maxHeight + height.roundToPx() * 2,
        ),
    )

    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
}

fun Modifier.extraSize(widthPercent: Float, heightPercent: Float) = this.layout { measurable, constraints ->
    val width = constraints.maxWidth * widthPercent
    val height = constraints.maxHeight * heightPercent

    val placeable = measurable.measure(
        constraints.copy(
            maxWidth = (constraints.maxWidth + width * 2).toInt(),
            maxHeight = (constraints.maxHeight + height * 2).toInt(),
        ),
    )

    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.marquee(): Modifier {
    return this
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithContent {
            drawContent()
            drawFadedEdge(leftEdge = true)
            drawFadedEdge(leftEdge = false)
        }
        .basicMarquee(Int.MAX_VALUE)
        .padding(horizontal = 8.dp)
}

fun ContentDrawScope.drawFadedEdge(leftEdge: Boolean) {
    val edgeWidthPx = 8.dp.toPx()
    drawRect(
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startX = if (leftEdge) 0f else size.width,
            endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx,
        ),
        blendMode = BlendMode.DstIn,
    )
}
