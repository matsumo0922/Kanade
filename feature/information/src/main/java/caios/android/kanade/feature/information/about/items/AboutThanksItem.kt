package caios.android.kanade.feature.information.about.items

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
internal fun AboutThanksItem(
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    iconPainter: Painter,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Image(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(50)),
            painter = iconPainter,
            contentDescription = null,
        )

        AboutDescriptionItem(
            modifier = Modifier.weight(1f),
            titleRes = titleRes,
            descriptionRes = descriptionRes,
            content = content,
        )
    }
}
