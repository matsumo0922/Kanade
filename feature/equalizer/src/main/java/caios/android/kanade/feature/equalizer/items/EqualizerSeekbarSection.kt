package caios.android.kanade.feature.equalizer.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.common.network.util.StringUtil.toHzString
import caios.android.kanade.core.model.music.Equalizer

@Composable
internal fun EqualizerSeekbarSection(
    equalizer: Equalizer,
    onValueChange: (Equalizer.Band, Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        for (band in equalizer.bands) {
            EqualizerSeekbar(
                value = band.value,
                hz = band.hz,
                onValueChange = { onValueChange.invoke(band, it)},
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun EqualizerSeekbar(
    value: Float,
    hz: Int,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "$value dB",
            style = MaterialTheme.typography.labelMedium,
        )

        Slider(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    rotationZ = 270f
                    transformOrigin = TransformOrigin(0f, 0f)
                }
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        Constraints(
                            minWidth = constraints.minHeight,
                            maxWidth = constraints.maxHeight,
                            minHeight = constraints.minWidth,
                            maxHeight = constraints.maxWidth,
                        )
                    )
                    layout(placeable.height, placeable.width) {
                        placeable.place(-placeable.width, 0)
                    }
                }
            ,
            value = value,
            valueRange = -150f..150f,
            onValueChange = { onValueChange.invoke(it) },
        )

        Text(
            text = hz.toHzString(),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
