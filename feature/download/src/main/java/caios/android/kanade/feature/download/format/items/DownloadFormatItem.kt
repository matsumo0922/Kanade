package caios.android.kanade.feature.download.format.items

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.common.network.util.StringUtil.toFileSizeString
import caios.android.kanade.core.model.download.VideoInfo

@Composable
internal fun DownloadFormatItem(
    format: VideoInfo.Format,
    isSelect: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val outlineColor by animateColorAsState(
        targetValue = if (isSelect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        label = "FormatItemOutline",
    )

    val titleColor by animateColorAsState(
        targetValue = if (isSelect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        label = "FormatItemTitle",
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelect) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        label = "FormatItemTitle",
    )

    val fileSize = format.fileSize?.toFloat()?.toFileSizeString()
    val bitRate = format.tbr?.toFloat()?.toFileSizeString()
    val vCodec = format.vcodec.toString().substringBefore(".")
    val aCodec = format.acodec.toString().substringBefore(".")
    val codec = vCodec + if (vCodec.isNotEmpty() && aCodec.isNotEmpty()) " " else "" + aCodec

    Column(
        modifier = modifier
            .background(backgroundColor)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 0.5.dp,
                color = outlineColor,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable { onSelect.invoke() }
            .padding(12.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = format.format.toString(),
            style = MaterialTheme.typography.titleSmall,
            color = titleColor,
            maxLines = 2,
            minLines = 2,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "$fileSize - $bitRate",
            style = MaterialTheme.typography.labelMedium,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = if (codec.isNotBlank()) "($codec)" else "",
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
