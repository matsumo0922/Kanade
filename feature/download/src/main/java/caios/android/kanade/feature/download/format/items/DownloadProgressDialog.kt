package caios.android.kanade.feature.download.format.items

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import caios.android.kanade.core.design.R
import coil.compose.AsyncImage

@Composable
internal fun DownloadProgressDialog(
    progress: Float,
    title: String,
    author: String?,
    thumbnail: String?,
    onClickTagEdit: () -> Unit,
    onDismiss: () -> Unit,
) {
    val animateProgress by animateFloatAsState(
        targetValue = progress,
        label = "DownloadProgress",
    )

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
                        top = 24.dp,
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
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(24.dp))

            when {
                progress <= 0f -> {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                progress < 1f -> {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = animateProgress,
                    )
                }
                else -> {
                    Row(
                        modifier = Modifier
                            .padding(
                                bottom = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                            )
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
}

@Composable
@Preview
private fun DownloadProgressDialogPreview1() {
    DownloadProgressDialog(
        progress = 0.5f,
        title = "Title",
        author = "Author",
        thumbnail = "https://i.ytimg.com/vi/0zGcUoRlhmw/maxresdefault.jpg",
        onClickTagEdit = { /* do nothing */ },
        onDismiss = { /* do nothing */ },
    )
}

@Composable
@Preview
private fun DownloadProgressDialogPreview2() {
    DownloadProgressDialog(
        progress = 1f,
        title = "Title",
        author = "Author",
        thumbnail = "https://i.ytimg.com/vi/0zGcUoRlhmw/maxresdefault.jpg",
        onClickTagEdit = { /* do nothing */ },
        onDismiss = { /* do nothing */ },
    )
}
