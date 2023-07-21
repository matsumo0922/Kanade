package caios.android.kanade.feature.setting.top.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.component.KanadeBackground

@Composable
internal fun SettingTextItem(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    val titleColor: Color
    val descriptionColor: Color

    if (isEnabled) {
        titleColor = MaterialTheme.colorScheme.onSurface
        descriptionColor = MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurface
            .copy(alpha = 0.38f)
            .compositeOver(MaterialTheme.colorScheme.surface)
            .also {
                titleColor = it
                descriptionColor = it
            }
    }

    Column(
        modifier = modifier
            .clickable(
                enabled = isEnabled,
                onClick = { onClick.invoke() },
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = titleColor,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = descriptionColor,
        )
    }
}

@Composable
internal fun SettingTextItem(
    @StringRes title: Int,
    @StringRes description: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    SettingTextItem(
        modifier = modifier,
        title = stringResource(title),
        description = stringResource(description),
        onClick = onClick,
        isEnabled = isEnabled,
    )
}

@Preview
@Composable
private fun PreviewSettingTextItem1() {
    KanadeBackground(backgroundColor = MaterialTheme.colorScheme.surface) {
        SettingTextItem(
            title = android.R.string.copy,
            description = android.R.string.copyUrl,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewSettingTextItem2() {
    KanadeBackground(backgroundColor = MaterialTheme.colorScheme.surface) {
        SettingTextItem(
            title = android.R.string.copy,
            description = android.R.string.copyUrl,
            onClick = {},
            isEnabled = false,
        )
    }
}
