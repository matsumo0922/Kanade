package caios.android.kanade.feature.setting.top.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.feature.setting.SettingSwitchItem

@Composable
internal fun SettingTopLibrarySection(
    userData: UserData,
    onClickIgnoreShotMusic: (Boolean) -> Unit,
    onClickIgnoreNotMusic: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        SettingTopTitleItem(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.setting_top_library,
        )

        SettingSwitchItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_library_ignore_short_music,
            description = R.string.setting_top_library_ignore_short_music_description,
            value = userData.isIgnoreShortMusic,
            onValueChanged = onClickIgnoreShotMusic,
        )

        SettingSwitchItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_library_ignore_not_music,
            description = R.string.setting_top_library_ignore_not_music_description,
            value = userData.isIgnoreNotMusic,
            onValueChanged = onClickIgnoreNotMusic,
        )
    }
}
