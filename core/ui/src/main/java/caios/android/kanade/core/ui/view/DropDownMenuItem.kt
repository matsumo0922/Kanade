package caios.android.kanade.core.ui.view

import androidx.annotation.StringRes
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun DropDownMenuItem(
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        modifier = modifier,
        onClick = onClick,
        text = {
            Text(
                text = stringResource(text),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
    )
}

data class DropDownMenuItemData(
    @StringRes val text: Int,
    val onClick: () -> Unit,
)
