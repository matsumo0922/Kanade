package caios.android.kanade.core.design.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class LibraryTopBarScrollState(
    initialYOffsetLimit: Float,
    initialYOffset: Float,
    initialContentOffset: Float,
) {
    var yOffsetLimit by mutableStateOf(initialYOffsetLimit)

    var yOffset: Float
        get() = _yOffset.value
        set(newOffset) {
            _yOffset.value = newOffset.coerceIn(
                minimumValue = yOffsetLimit,
                maximumValue = 0f
            )
        }

    var contentOffset by mutableStateOf(initialContentOffset)

    private var _yOffset = mutableStateOf(initialYOffset)

    companion object {
        val Saver: Saver<LibraryTopBarScrollState, *> = listSaver(
            save = { listOf(it.yOffsetLimit, it.yOffset, it.contentOffset) },
            restore = {
                LibraryTopBarScrollState(
                    initialYOffsetLimit = it[0],
                    initialYOffset = it[1],
                    initialContentOffset = it[2]
                )
            }
        )
    }
}

@Composable
fun rememberLibraryTopBarScrollState(
    initialYOffsetLimit: Float = -Float.MAX_VALUE,
    initialYOffset: Float = 0f,
    initialContentOffset: Float = 0f
) = rememberSaveable(saver = LibraryTopBarScrollState.Saver) {
    LibraryTopBarScrollState(
        initialYOffsetLimit = initialYOffsetLimit,
        initialYOffset = initialYOffset,
        initialContentOffset = initialContentOffset,
    )
}