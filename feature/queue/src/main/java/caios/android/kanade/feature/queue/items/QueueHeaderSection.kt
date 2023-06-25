package caios.android.kanade.feature.queue.items

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.design.theme.center

@Composable
internal fun QueueHeaderSection(
    onClickDismiss: () -> Unit,
    onClickMenuAddPlaylist: () -> Unit,
    onClickMenuShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpandedMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClickDismiss.invoke() }
                .padding(4.dp),
            imageVector = Icons.Default.ExpandMore,
            contentDescription = null,
        )

        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.queue_title),
            style = MaterialTheme.typography.bodyLarge.bold().center(),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Box {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { isExpandedMenu = !isExpandedMenu }
                    .padding(4.dp),
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
            )

            DropdownMenu(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(MaterialTheme.colorScheme.surface),
                expanded = isExpandedMenu,
                onDismissRequest = { isExpandedMenu = false },
            ) {
                DropDownMenuItem(
                    text = R.string.queue_menu_add_to_playlist,
                    onClick = onClickMenuAddPlaylist,
                )

                DropDownMenuItem(
                    text = R.string.queue_menu_share,
                    onClick = onClickMenuShare,
                )
            }
        }
    }
}

@Composable
private fun DropDownMenuItem(
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

@Preview
@Composable
private fun QueueHeaderSectionPreview() {
    QueueHeaderSection(
        onClickDismiss = {},
        onClickMenuAddPlaylist = {},
        onClickMenuShare = {},
    )
}
