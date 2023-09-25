package caios.android.kanade.feature.setting.top

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.feature.setting.SettingTheme
import caios.android.kanade.feature.setting.top.items.SettingTopLibrarySection
import caios.android.kanade.feature.setting.top.items.SettingTopOthersSection
import caios.android.kanade.feature.setting.top.items.SettingTopPlayingSection
import caios.android.kanade.feature.setting.top.items.SettingTopThemeSection

@Composable
internal fun SettingTopRoute(
    navigateToEqualizer: () -> Unit,
    navigateToSettingTheme: () -> Unit,
    navigateToSettingDeveloper: () -> Unit,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingTopViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
    ) { uiState ->
        SettingTopScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            userData = uiState.userData,
            config = uiState.config,
            onClickTheme = navigateToSettingTheme,
            onClickEqualizer = navigateToEqualizer,
            onClickDynamicNormalizer = viewModel::setUseDynamicNormalizer,
            onClickOneStepBack = viewModel::setOneStepBack,
            onClickKeepAudioFocus = viewModel::setKeepAudioFocus,
            onClickStopWhenTaskkill = viewModel::setStopWhenTaskkill,
            onClickIgnoreShortMusic = viewModel::setIgnoreShortMusic,
            onClickIgnoreNotMusic = viewModel::setIgnoreNotMusic,
            onClickYtDlpVersion = viewModel::updateYoutubeDL,
            onClickDeveloperMode = { isEnable ->
                if (isEnable) {
                    navigateToSettingDeveloper()
                } else {
                    viewModel.setDeveloperMode(false)
                }
            },
            onTerminate = terminate,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingTopScreen(
    userData: UserData,
    config: KanadeConfig,
    onClickTheme: () -> Unit,
    onClickEqualizer: () -> Unit,
    onClickDynamicNormalizer: (Boolean) -> Unit,
    onClickOneStepBack: (Boolean) -> Unit,
    onClickKeepAudioFocus: (Boolean) -> Unit,
    onClickStopWhenTaskkill: (Boolean) -> Unit,
    onClickIgnoreShortMusic: (Boolean) -> Unit,
    onClickIgnoreNotMusic: (Boolean) -> Unit,
    onClickYtDlpVersion: suspend (Context) -> String?,
    onClickDeveloperMode: (Boolean) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state)

    Scaffold(
        modifier = modifier.nestedScroll(behavior.nestedScrollConnection),
        topBar = {
            SettingTheme {
                LargeTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = stringResource(R.string.setting_title),
                        )
                    },
                    navigationIcon = {
                        Icon(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(50))
                                .padding(6.dp)
                                .clickable { onTerminate.invoke() },
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    scrollBehavior = behavior,
                )
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            item {
                SettingTopThemeSection(
                    modifier = Modifier.fillMaxWidth(),
                    onClickAppTheme = onClickTheme,
                )

                SettingTopPlayingSection(
                    modifier = Modifier.fillMaxWidth(),
                    userData = userData,
                    onClickEqualizer = onClickEqualizer,
                    onClickDynamicNormalizer = onClickDynamicNormalizer,
                    onClickOneStepBack = onClickOneStepBack,
                    onClickKeepAudioFocus = onClickKeepAudioFocus,
                    onClickStopWhenTaskkill = onClickStopWhenTaskkill,
                )

                SettingTopLibrarySection(
                    modifier = Modifier.fillMaxWidth(),
                    userData = userData,
                    onClickIgnoreShotMusic = onClickIgnoreShortMusic,
                    onClickIgnoreNotMusic = onClickIgnoreNotMusic,
                )

                SettingTopOthersSection(
                    modifier = Modifier.fillMaxWidth(),
                    config = config,
                    userData = userData,
                    onClickYtDlpVersion = onClickYtDlpVersion,
                    onClickDeveloperMode = onClickDeveloperMode,
                )
            }
        }
    }
}
