package caios.android.kanade.feature.lyrics.top.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun LyricsTopButtonSection(
    onClickExplore: () -> Unit,
    onClickDownload: () -> Unit,
    onClickPaste: () -> Unit,
    onClickSelectAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 24.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable { onClickExplore.invoke() }
                .padding(8.dp)
                .weight(1f),
            imageVector = Icons.Default.TravelExplore,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable { onClickDownload.invoke() }
                .padding(8.dp)
                .weight(1f),
            imageVector = Icons.Default.Download,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable { onClickPaste.invoke() }
                .padding(8.dp)
                .weight(1f),
            imageVector = Icons.Default.ContentPaste,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable { onClickSelectAll.invoke() }
                .padding(8.dp)
                .weight(1f),
            imageVector = Icons.Default.SelectAll,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LyricsTopButtonSectionPreview() {
    LyricsTopButtonSection(
        modifier = Modifier.fillMaxWidth(),
        onClickExplore = {},
        onClickDownload = {},
        onClickPaste = {},
        onClickSelectAll = {},
    )
}
