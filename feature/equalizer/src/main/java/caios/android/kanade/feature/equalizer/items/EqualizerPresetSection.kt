package caios.android.kanade.feature.equalizer.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.applyTonalElevation
import caios.android.kanade.core.model.music.Equalizer
import caios.android.kanade.core.ui.view.DropDownMenuItem

@Composable
internal fun EqualizerPresetSection(
    preset: Equalizer.Preset,
    onClickPreset: (Equalizer.Preset) -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentPreset by remember { mutableStateOf(preset) }
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        OutlinedTextField(
            modifier = Modifier.clickable { isExpanded = !isExpanded },
            value = stringResource(getPresetName(currentPreset)),
            onValueChange = { /* do nothing */ },
            readOnly = true,
            singleLine = true,
            label = { Text(stringResource(R.string.equalizer_preset)) },
        )

        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.applyTonalElevation(
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        elevation = 3.dp,
                    ),
                ),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            for (preset in Equalizer.Preset.entries) {
                DropDownMenuItem(
                    text = getPresetName(preset),
                    onClick = {
                        isExpanded = false
                        currentPreset = preset

                        onClickPreset.invoke(preset)
                    },
                )
            }
        }
    }
}

private fun getPresetName(preset: Equalizer.Preset): Int {
    return when (preset) {
        Equalizer.Preset.NONE -> R.string.equalizer_preset_normal
        Equalizer.Preset.CLASSICAL -> R.string.equalizer_preset_classical
        Equalizer.Preset.DANCE -> R.string.equalizer_preset_dance
        Equalizer.Preset.HEAVY_METAL -> R.string.equalizer_preset_heavy_metal
        Equalizer.Preset.HIP_HOP -> R.string.equalizer_preset_hip_hop
        Equalizer.Preset.JAZZ -> R.string.equalizer_preset_jazz
        Equalizer.Preset.POP -> R.string.equalizer_preset_pop
        Equalizer.Preset.ROCK -> R.string.equalizer_preset_rock
        Equalizer.Preset.CUSTOM -> R.string.equalizer_preset_custom
    }
}
