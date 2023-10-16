package caios.android.kanade.feature.setting.top.items

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.feature.setting.SettingSwitchItem
import caios.android.kanade.feature.setting.SettingTextItem
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.launch

@Composable
internal fun SettingTopOthersSection(
    userData: UserData,
    config: KanadeConfig,
    onClickYtDlpVersion: suspend (Context) -> String?,
    onClickOpenSourceLicense: () -> Unit,
    onClickDeveloperMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var ytDlpVersion by remember { mutableStateOf(YoutubeDL.getInstance().version(context)) }

    Column(modifier) {
        SettingTopTitleItem(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.setting_top_others,
        )

        SettingTextItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.setting_top_others_id),
            description = userData.kanadeId,
            onClick = { /* do nothing */ },
        )

        SettingTextItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.setting_top_others_version),
            description = "${config.versionName}:${config.versionCode}" + when {
                userData.isPlusMode && userData.isDeveloperMode -> " [P+D]"
                userData.isPlusMode -> " [Premium]"
                userData.isDeveloperMode -> " [Developer]"
                else -> ""
            },
            onClick = { /* do nothing */ },
        )

        SettingTextItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.setting_top_others_ytdlp_version),
            description = ytDlpVersion ?: "Unknown",
            onClick = {
                scope.launch {
                    ytDlpVersion = context.getString(R.string.setting_top_others_ytdlp_version_checking)
                    ytDlpVersion = onClickYtDlpVersion.invoke(context)
                }
            },
        )

        SettingTextItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.setting_top_others_open_source_license),
            description = stringResource(R.string.setting_top_others_open_source_license_description),
            onClick = { onClickOpenSourceLicense.invoke() },
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
