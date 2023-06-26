package caios.android.kanade.core.ui.music

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.bold

@Composable
fun AlbumDetailHeader(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f),
            text = stringResource(R.string.common_albums),
            style = MaterialTheme.typography.titleLarge.bold(),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
private fun AlbumDetailHeaderPreview() {
    KanadeBackground {
        AlbumDetailHeader(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
