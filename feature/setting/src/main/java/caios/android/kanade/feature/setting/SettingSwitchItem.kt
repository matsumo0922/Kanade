package caios.android.kanade.feature.setting

import android.R
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
internal fun SettingSwitchItem(
    @StringRes title: Int,
    @StringRes description: Int?,
    value: Boolean,
    onValueChanged: (Boolean) -> Unit,
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

    Row(
        modifier = modifier
            .clickable(
                enabled = isEnabled,
                onClick = { onValueChanged.invoke(!value) },
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                space = 4.dp,
                alignment = Alignment.CenterVertically,
            ),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(title),
                style = MaterialTheme.typography.bodyLarge,
                color = titleColor,
            )

            if (description != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = descriptionColor,
                )
            }
        }

        Switch(
            enabled = isEnabled,
            checked = value,
            onCheckedChange = { onValueChanged.invoke(it) },
        )
    }
}

@Preview
@Composable
private fun PreviewSettingSwitchItem1() {
    KanadeBackground(background = MaterialTheme.colorScheme.surface) {
        SettingSwitchItem(
            title = R.string.copy,
            description = R.string.copyUrl,
            value = true,
            onValueChanged = {},
        )
    }
}

@Preview
@Composable
private fun PreviewSettingSwitchItem2() {
    KanadeBackground(background = MaterialTheme.colorScheme.surface) {
        SettingSwitchItem(
            title = R.string.copy,
            description = R.string.copyUrl,
            value = true,
            onValueChanged = {},
            isEnabled = false,
        )
    }
}

@Preview
@Composable
private fun PreviewSettingSwitchItem3() {
    KanadeBackground(background = MaterialTheme.colorScheme.surface) {
        SettingSwitchItem(
            title = R.string.copy,
            description = null,
            value = true,
            onValueChanged = {},
        )
    }
}
