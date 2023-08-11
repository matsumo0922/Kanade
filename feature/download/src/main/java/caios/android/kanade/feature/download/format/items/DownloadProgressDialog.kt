package caios.android.kanade.feature.download.format.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import caios.android.kanade.core.design.R
import caios.android.kanade.feature.download.format.DownloadFormatUiState
import coil.compose.AsyncImage

@Composable
internal fun DownloadProgressDialog(
    state: DownloadFormatUiState.DownloadState.Progress,
    title: String,
    author: String?,
    thumbnail: String?,
    onClickTagEdit: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = { /* do nothing */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
                AsyncImage(
                    modifier = Modifier.aspectRatio(16f / 9f),
                    model = thumbnail,
                    contentDescription = "Video Thumbnail",
                )
            }

            Text(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                    )
                    .fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp,
                    )
                    .fillMaxWidth(),
                text = author ?: "Unknown Author",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (state.progress < 1f) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    progress = state.progress,
                )
            } else {
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        onClick = {
                            onDismiss.invoke()
                            onClickTagEdit.invoke()
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.download_progress_tag_edit),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        onClick = { onDismiss.invoke() },
                    ) {
                        Text(
                            text = stringResource(R.string.common_finish),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }
    }
}
