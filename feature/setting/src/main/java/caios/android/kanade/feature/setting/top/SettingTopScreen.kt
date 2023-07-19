package caios.android.kanade.feature.setting.top

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.icon.Palette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingTopScreen(
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state)

    val data = listOf(
        SettingItem(
            title = R.string.setting_top_theme,
            description = R.string.setting_top_theme_description,
            icon = Icons.Outlined.Palette,
            onClick = { },
        ),
        SettingItem(
            title = R.string.setting_top_playing,
            description = R.string.setting_top_playing_description,
            icon = Icons.Outlined.PlayArrow,
            onClick = { },
        ),
        SettingItem(
            title = R.string.setting_top_library,
            description = R.string.setting_top_library_description,
            icon = Icons.Outlined.Book,
            onClick = { },
        ),
        SettingItem(
            title = R.string.setting_top_others,
            description = R.string.setting_top_others_description,
            icon = Icons.Outlined.Info,
            onClick = { },
        ),
    )

    Scaffold(
        modifier = modifier.nestedScroll(behavior.nestedScrollConnection),
        topBar = {
            SettingTheme {
                LargeTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = stringResource(R.string.setting_title),
                        )
                    },
                    navigationIcon = {
                        Icon(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(50))
                                .padding(6.dp)
                                .clickable { terminate.invoke() },
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    scrollBehavior = behavior,
                )
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            items(
                items = data,
                key = { item -> item.title },
            ) {
                SettingItem(
                    modifier = Modifier.fillMaxWidth(),
                    data = it,
                )
            }
        }
    }
}

@Composable
private fun SettingItem(
    data: SettingItem,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable { data.onClick.invoke() }
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Icon(
            modifier = Modifier.size(26.dp),
            imageVector = data.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(data.title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(data.description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SettingTheme(content: @Composable () -> Unit) {
    val typography = MaterialTheme.typography.copy(
        titleLarge = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.1.sp,
        ),
        headlineMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp,
        ),
    )

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = typography,
    ) {
        content.invoke()
    }
}

private data class SettingItem(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val icon: ImageVector,
    val onClick: () -> Unit,
)
