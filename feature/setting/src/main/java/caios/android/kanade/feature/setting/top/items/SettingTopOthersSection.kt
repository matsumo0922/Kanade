package caios.android.kanade.feature.setting.top.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.feature.setting.top.settings.SettingSwitchItem
import caios.android.kanade.feature.setting.top.settings.SettingTextItem

@Composable
internal fun SettingTopOthersSection(
    userData: UserData,
    config: KanadeConfig,
    onClickDeveloperMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        SettingTopTitleItem(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.setting_top_others,
        )

        SettingTextItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.setting_top_others_id),
            description = "Unknown",
            onClick = { /* do nothing */ },
        )

        SettingTextItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.setting_top_others_version),
            description = "${config.versionName}:${config.versionCode}" + when {
                userData.isPremiumMode && userData.isDeveloperMode -> "[P+D]"
                userData.isPremiumMode -> "[Premium]"
                userData.isDeveloperMode -> "[Developer]"
                else -> ""
            },
            onClick = { /* do nothing */ },
        )

        SettingSwitchItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_others_developer_mode,
            description = R.string.setting_top_others_developer_mode_description,
            value = userData.isDeveloperMode,
            onValueChanged = onClickDeveloperMode,
        )
    }
}
