package caios.android.kanade.core.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.theme.applyTonalElevation
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanadeTopAppBar(
    title: String,
    behavior: TopAppBarScrollBehavior,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
    dropDownMenuItems: ImmutableList<DropDownMenuItemData> = persistentListOf(),
) {
    var isExpandedMenu by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(50))
                    .clickable { onTerminate.invoke() }
                    .padding(8.dp),
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
            )
        },
        actions = {
            if (dropDownMenuItems.isNotEmpty()) {
                Box {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(50))
                            .clickable { isExpandedMenu = !isExpandedMenu }
                            .padding(8.dp),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                    )

                    DropdownMenu(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(
                                MaterialTheme.colorScheme.applyTonalElevation(
                                    backgroundColor = MaterialTheme.colorScheme.surface,
                                    elevation = 3.dp,
                                ),
                            ),
                        expanded = isExpandedMenu,
                        onDismissRequest = { isExpandedMenu = false },
                    ) {
                        dropDownMenuItems.forEach {
                            DropDownMenuItem(
                                text = it.text,
                                onClick = {
                                    isExpandedMenu = false
                                    it.onClick.invoke()
                                },
                            )
                        }
                    }
                }
            }
        },
        scrollBehavior = behavior,
    )
}
