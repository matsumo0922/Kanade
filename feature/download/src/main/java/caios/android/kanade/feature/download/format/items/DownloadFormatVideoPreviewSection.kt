package caios.android.kanade.feature.download.format.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.common.network.util.StringUtil.toTimeString
import coil.compose.AsyncImage

@Composable
internal fun DownloadFormatVideoPreviewSection(
    title: String,
    author: String?,
    duration: Long?,
    thumbnail: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(Modifier.weight(1f)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
                AsyncImage(
                    modifier = Modifier.aspectRatio(16f / 9f),
                    model = thumbnail,
                    contentDescription = "Video Thumbnail",
                )
            }

            Card(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(Color.Black.copy(alpha = 0.8f)),
            ) {
                Text(
                    modifier = Modifier.padding(4.dp, 2.dp),
                    text = duration?.toTimeString() ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.bodyMedium,
            )

            if (author != null && author != "playlist") {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
