package caios.android.kanade.core.design.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class LibraryTopBarScrollState(
    initialYOffset: Float,
    initialScrolledQuantity: Float,
) {
    var yOffset by mutableFloatStateOf(initialYOffset)
    var scrolledQuantity by mutableFloatStateOf(initialScrolledQuantity)

    companion object {
        val Saver: Saver<LibraryTopBarScrollState, *> = listSaver(
            save = { listOf(it.yOffset, it.scrolledQuantity) },
            restore = {
                LibraryTopBarScrollState(
                    initialYOffset = it[0],
                    initialScrolledQuantity = it[1],
                )
            },
        )
    }
}

@Composable
fun rememberLibraryTopBarScrollState(
    initialYOffset: Float = 0f,
    initialContentOffset: Float = 0f,
) = rememberSaveable(saver = LibraryTopBarScrollState.Saver) {
    LibraryTopBarScrollState(
        initialYOffset = initialYOffset,
        initialScrolledQuantity = initialContentOffset,
    )
}
