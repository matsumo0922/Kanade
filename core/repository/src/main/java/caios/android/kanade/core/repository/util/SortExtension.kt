package caios.android.kanade.core.repository.util

import caios.android.kanade.core.model.Order

internal fun <T> List<T>.sortList(vararg selectors: (T) -> Comparable<*>?, order: Order): List<T> {
    val comparator = when (order) {
        Order.ASC -> compareBy(*selectors)
        Order.DESC -> {
            val returnComparator = compareByDescending(selectors[0])
            val remainSelectors = selectors.toMutableList().apply { removeFirst() }

            for (selector in remainSelectors) {
                returnComparator.thenByDescending(selector)
            }

            returnComparator
        }
    }

    return sortedWith(comparator)
}
