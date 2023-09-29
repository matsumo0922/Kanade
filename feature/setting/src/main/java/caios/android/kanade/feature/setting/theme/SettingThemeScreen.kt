package caios.android.kanade.feature.setting.theme

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ThemeColorConfig
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.feature.setting.SettingSwitchItem
import caios.android.kanade.feature.setting.SettingTheme
import caios.android.kanade.feature.setting.theme.items.SettingThemeColorSection
import caios.android.kanade.feature.setting.theme.items.SettingThemeTabsSection

@Composable
internal fun SettingThemeRoute(
    navigateToBillingPlus: () -> Unit,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingThemeViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
    ) {
        SettingThemeDialog(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            userData = it.userData,
            onBillingPlus = navigateToBillingPlus,
            onSelectTheme = viewModel::setThemeConfig,
            onSelectThemeColor = viewModel::setThemeColorConfig,
            onClickDynamicColor = viewModel::setUseDynamicColor,
            onTerminate = terminate,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingThemeDialog(
    userData: UserData,
    onBillingPlus: () -> Unit,
    onSelectTheme: (ThemeConfig) -> Unit,
    onSelectThemeColor: (ThemeColorConfig) -> Unit,
    onClickDynamicColor: (Boolean) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
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
                            text = stringResource(R.string.setting_theme_title),
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
                SettingThemeTabsSection(
                    modifier = Modifier.fillMaxWidth(),
                    themeConfig = userData.themeConfig,
                    onSelectTheme = onSelectTheme,
                )
            }

            item {
                SettingSwitchItem(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    title = R.string.setting_theme_theme_dynamic_color,
                    description = R.string.setting_theme_theme_dynamic_color_description,
                    value = userData.isDynamicColor,
                    onValueChanged = {
                        if (userData.hasPrivilege) {
                            onClickDynamicColor.invoke(it)
                        } else {
                            ToastUtil.show(context, R.string.billing_plus_toast_require_plus)
                            onBillingPlus.invoke()
                        }
                    },
                )
            }

            item {
                SettingThemeColorSection(
                    modifier = Modifier.fillMaxWidth(),
                    isUseDynamicColor = userData.isDynamicColor,
                    themeConfig = userData.themeConfig,
                    themeColorConfig = userData.themeColorConfig,
                    onSelectThemeColor = onSelectThemeColor,
                )
            }
        }
    }
}
