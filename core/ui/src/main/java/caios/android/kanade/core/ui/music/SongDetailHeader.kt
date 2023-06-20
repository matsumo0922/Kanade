package caios.android.kanade.core.ui.music

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.bold

@Composable
fun SongDetailHeader(
    onClickSeeAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            text = stringResource(R.string.common_songs),
            style = MaterialTheme.typography.titleMedium.bold(),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable { onClickSeeAll.invoke() }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(end = 8.dp),
                text = stringResource(R.string.song_header_see_all),
                style = MaterialTheme.typography.bodyMedium.bold(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
private fun SongDetailHeaderPreview() {
    KanadeBackground {
        SongDetailHeader(
            modifier = Modifier.fillMaxWidth(),
            onClickSeeAll = {},
        )
    }
}
