package caios.android.kanade.core.ui.controller.items

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import caios.android.kanade.core.design.component.KanadeBackground

@Composable
internal fun MainControllerToolBarSection(
    onClickClose: () -> Unit,
    onClickSearch: () -> Unit,
    onClickMenuAddPlaylist: () -> Unit,
    onClickMenuArtist: () -> Unit,
    onClickMenuAlbum: () -> Unit,
    onClickMenuEqualizer: () -> Unit,
    onClickMenuEdit: () -> Unit,
    onClickMenuAnalyze: () -> Unit,
    onClickMenuDetailInfo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClickClose.invoke() }
                .padding(4.dp),
            imageVector = Icons.Default.ExpandMore,
            contentDescription = null,
        )

        Spacer(Modifier.weight(1f))

        Icon(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClickSearch.invoke() }
                .padding(4.dp),
            imageVector = Icons.Default.Search,
            contentDescription = null,
        )

        Box {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { expandedMenu = !expandedMenu }
                    .padding(4.dp),
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
            )

            DropdownMenu(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(MaterialTheme.colorScheme.surface),
                expanded = expandedMenu,
                onDismissRequest = { expandedMenu = false },
            ) {
                DropDownMenuItem(
                    text = R.string.controller_menu_add_playlist,
                    onClick = onClickMenuAddPlaylist,
                )

                DropDownMenuItem(
                    text = R.string.controller_menu_artist,
                    onClick = onClickMenuArtist,
                )

                DropDownMenuItem(
                    text = R.string.controller_menu_album,
                    onClick = onClickMenuAlbum,
                )

                DropDownMenuItem(
                    text = R.string.controller_menu_equalizer,
                    onClick = onClickMenuEqualizer,
                )

                DropDownMenuItem(
                    text = R.string.controller_menu_edit,
                    onClick = onClickMenuEdit,
                )

                DropDownMenuItem(
                    text = R.string.controller_menu_analyze,
                    onClick = onClickMenuAnalyze,
                )

                DropDownMenuItem(
                    text = R.string.controller_menu_detail_info,
                    onClick = onClickMenuDetailInfo,
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

@Preview(showBackground = true)
@Composable
private fun Preview() {
    KanadeBackground {
        MainControllerToolBarSection(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onClickClose = { },
            onClickSearch = { },
            onClickMenuAddPlaylist = { },
            onClickMenuArtist = { },
            onClickMenuAlbum = { },
            onClickMenuEqualizer = { },
            onClickMenuEdit = { },
            onClickMenuAnalyze = { },
            onClickMenuDetailInfo = { },
        )
    }
}
