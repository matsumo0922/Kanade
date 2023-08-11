package caios.android.kanade.feature.download.format.items

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
internal fun DownloadFormatSubTitle(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(horizontal = 8.dp),
        text = stringResource(title),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
    )
}
