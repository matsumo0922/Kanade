package caios.android.kanade.core.ui.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

inline fun <T> LazyGridScope.itemsWithEdgeSpace(
    @androidx.annotation.IntRange(from = 1) spanCount: Int,
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyGridItemScope.(item: T) -> Unit,
) {
    require(spanCount > 0)

    val itemSize = items.size
    val rowCount = if (itemSize % spanCount == 0) itemSize / spanCount else itemSize / spanCount + 1

    for (index in 0..<rowCount) {
        item { Spacer(modifier = Modifier.fillMaxWidth()) }

        val childItems = items.subList(
            index * spanCount,
            minOf(index * spanCount + spanCount, itemSize),
        )

        items(childItems, key, span, contentType, itemContent)

        if (index != rowCount - 1 || itemSize % spanCount != 0) {
            item { Spacer(modifier = Modifier.fillMaxWidth()) }
        }
    }
}

inline fun <T> LazyGridScope.itemsIndexedWithEdgeSpace(
    @androidx.annotation.IntRange(from = 1) spanCount: Int,
    items: List<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(index: Int, item: T) -> GridItemSpan)? = null,
    noinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyGridItemScope.(index: Int, item: T) -> Unit
) {
    require(spanCount > 0)

    val itemSize = items.size
    val rowCount = if (itemSize % spanCount == 0) itemSize / spanCount else itemSize / spanCount + 1

    for (index in 0..<rowCount) {
        item { Spacer(modifier = Modifier.fillMaxWidth()) }

        val childItems = items.subList(
            index * spanCount,
            minOf(index * spanCount + spanCount, itemSize),
        )

        itemsIndexed(childItems, key, span, contentType, itemContent)

        if (index != rowCount - 1 || itemSize % spanCount != 0) {
            item { Spacer(modifier = Modifier.fillMaxWidth()) }
        }
    }
}
