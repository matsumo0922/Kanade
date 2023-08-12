package caios.android.kanade.feature.equalizer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Equalizer
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.view.KanadeTopAppBar
import caios.android.kanade.feature.equalizer.items.EqualizerPresetSection
import caios.android.kanade.feature.equalizer.items.EqualizerSeekbarSection

@Composable
internal fun EqualizerRoute(
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EqualizerViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
    ) {
        if (it != null) {
            EqualizerScreen(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                equalizer = it.equalizer,
                onClickPreset = viewModel::updatePreset,
                onBandValueChanged = viewModel::updateBand,
                onTerminate = terminate,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EqualizerScreen(
    equalizer: Equalizer,
    onClickPreset: (Equalizer.Preset) -> Unit,
    onBandValueChanged: (Equalizer.Band, Float) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appBarState = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(appBarState)

    Scaffold(
        modifier = modifier.nestedScroll(behavior.nestedScrollConnection),
        topBar = {
            KanadeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.equalizer_title),
                behavior = behavior,
                onTerminate = onTerminate,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            EqualizerPresetSection(
                modifier = Modifier.fillMaxWidth(),
                preset = equalizer.preset,
                onClickPreset = onClickPreset,
            )

            EqualizerSeekbarSection(
                modifier = Modifier.fillMaxWidth(),
                equalizer = equalizer,
                onValueChange = onBandValueChanged,
            )
        }
    }
}
