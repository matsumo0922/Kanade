package caios.android.kanade.feature.information.versions

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.center
import caios.android.kanade.core.design.theme.end
import caios.android.kanade.core.design.theme.start
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.Version
import caios.android.kanade.core.ui.dialog.showAsButtonSheet
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.format.DateTimeFormatter

@Composable
private fun VersionHistoryDialog(
    versionHistory: ImmutableList<Version>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            text = stringResource(R.string.about_support_version_history),
            style = MaterialTheme.typography.bodyLarge.center(),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Divider()

        LazyColumn(Modifier.weight(1f)) {
            items(
                items = versionHistory,
                key = { item -> item.code },
            ) {
                VersionItem(
                    modifier = Modifier.fillMaxWidth(),
                    version = it,
                )
            }
        }
    }
}

@Composable
private fun VersionItem(
    version: Version,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${version.name} [${version.code}]",
                    style = MaterialTheme.typography.bodyMedium.start(),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    modifier = Modifier.weight(1f),
                    text = version.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                    style = MaterialTheme.typography.bodyMedium.end(),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = version.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Divider()
    }
}

fun Activity.showVersionHistoryDialog(
    userData: UserData?,
    versionHistory: ImmutableList<Version>,
) {
    showAsButtonSheet(userData, rectCorner = true) { _ ->
        VersionHistoryDialog(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            versionHistory = versionHistory.reversed().toImmutableList(),
        )
    }
}
