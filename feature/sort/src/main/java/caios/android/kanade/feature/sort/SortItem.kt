package caios.android.kanade.feature.sort

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
internal fun SortItemSection(
    @StringRes titleRes: Int,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
) {
    val containerColor: Color
    val contentColor: Color

    if (isSelected) {
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        contentColor = MaterialTheme.colorScheme.primary
    } else {
        containerColor = MaterialTheme.colorScheme.surface
        contentColor = MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = modifier
            .background(containerColor)
            .clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (imageVector != null) {
            Icon(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp, start = 18.dp)
                    .size(22.dp, 22.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = contentColor,
            )
        } else {
            Spacer(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp, start = 18.dp)
                    .size(22.dp, 22.dp),
            )
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 18.dp)
                .fillMaxWidth(),
            text = stringResource(titleRes),
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
        )
    }
}
