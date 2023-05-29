package caios.android.kanade.core.design.component

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

class LibraryTopBarScrollBehavior(
    private val state: LibraryTopBarScrollState,
) {

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val prevHeightOffset = state.yOffset

            state.yOffset = state.yOffset + available.y


            return if (prevHeightOffset != state.yOffset) {
                // We're in the middle of top app bar collapse or expand.
                // Consume only the scroll on the Y axis.
                available.copy(x = 0f)
            } else {
                Offset.Zero
            }
        }
    }
}