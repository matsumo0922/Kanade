package caios.android.kanade.feature.sort

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.Order

@Composable
internal fun SortOrderSection(
    order: Order,
    onClickOrder: (Order) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        SortItemSection(
            modifier = Modifier.fillMaxWidth(),
            titleRes = R.string.sort_order_asc,
            imageVector = Icons.Default.ArrowUpward,
            isSelected = order == Order.ASC,
            onClick = { onClickOrder.invoke(Order.ASC) },
        )

        SortItemSection(
            modifier = Modifier.fillMaxWidth(),
            titleRes = R.string.sort_order_desc,
            imageVector = Icons.Default.ArrowDownward,
            isSelected = order == Order.DESC,
            onClick = { onClickOrder.invoke(Order.DESC) },
        )
    }
}

@Preview
@Composable
private fun SortOrderSectionPreview() {
    KanadeBackground(background = MaterialTheme.colorScheme.surface) {
        SortOrderSection(
            modifier = Modifier.fillMaxWidth(),
            order = Order.ASC,
            onClickOrder = {},
        )
    }
}
