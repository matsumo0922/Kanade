package caios.android.kanade.feature.setting.top.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.feature.setting.SettingSwitchItem
import caios.android.kanade.feature.setting.SettingTextItem

@Composable
internal fun SettingTopYTMusicSection(
    userData: UserData,
    onClickEnableYTMusic: (Boolean) -> Unit,
    onClickRemoveYTMusicToken: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(modifier) {
        SettingTopTitleItem(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.setting_top_ytmusic,
        )

        SettingSwitchItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_library_ytmusic_title,
            description = R.string.setting_top_library_ytmusic_description,
            value = userData.isEnableYTMusic,
            onValueChanged = onClickEnableYTMusic,
        )

        SettingTextItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_library_ytmusic_remove_token_title,
            description = R.string.setting_top_library_ytmusic_remove_token_description,
            onClick = {
                ToastUtil.show(context, R.string.setting_ytmusic_login_toast_remove_token)
                onClickRemoveYTMusicToken.invoke()
            },
        )
    }
}
