package caios.android.kanade.core.design.component

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import kotlin.math.max
import kotlin.math.min

class LibraryTopBarScrollBehavior(
    val state: LibraryTopBarScrollState,
    val topBarHeight: Float,
) {

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            state.scrolledQuantity = state.scrolledQuantity + available.y
            state.yOffset = min(0f, max(-topBarHeight, state.yOffset + available.y))

            return Offset.Zero
        }
    }
}
