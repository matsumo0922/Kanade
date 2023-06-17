package caios.android.kanade.core.ui.view

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class FixedWithEdgeSpace(
    private val count: Int,
    private val edgeSpace: Dp = 0.dp
) : GridCells {

    init {
        require(count > 0)
    }

    override fun Density.calculateCrossAxisCellSizes(availableSize: Int, spacing: Int): List<Int> {
        val fixedCount = count + 2 // 両端のSpacer部分をプラスする
        val edgeSpacing = edgeSpace.roundToPx()
        val gridSizeWithoutSpacing = availableSize - spacing * (fixedCount - 1) - edgeSpacing * 2
        val slotSize = gridSizeWithoutSpacing / count
        val remainingPixels = gridSizeWithoutSpacing % count
        return List(fixedCount) {
            if (it == 0 || it == fixedCount - 1) {
                edgeSpacing // Spacer部分の幅を指定する
            } else {
                slotSize + if (it - 1 < remainingPixels) 1 else 0 // Spacer部分以外のitemは均等幅にする
            }
        }
    }
}
