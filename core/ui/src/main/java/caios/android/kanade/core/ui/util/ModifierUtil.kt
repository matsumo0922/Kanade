@file:Suppress("MatchingDeclarationName")

package caios.android.kanade.core.ui.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.extraSize(width: Dp, height: Dp) = this.layout { measurable, constraints ->
    val placeable = measurable.measure(
        constraints.copy(
            maxWidth = constraints.maxWidth + width.roundToPx() * 2,
            maxHeight = constraints.maxHeight + height.roundToPx() * 2
        )
    )

    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
}

fun Modifier.extraSize(widthPercent: Float, heightPercent: Float) = this.layout { measurable, constraints ->
    val width = constraints.maxWidth * widthPercent
    val height = constraints.maxHeight * heightPercent

    val placeable = measurable.measure(
        constraints.copy(
            maxWidth = (constraints.maxWidth + width * 2).toInt(),
            maxHeight = (constraints.maxHeight + height * 2).toInt()
        )
    )

    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
}