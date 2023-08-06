package caios.android.kanade.feature.setting.top.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import caios.android.kanade.core.design.R
import caios.android.kanade.feature.setting.SettingTextItem

@Composable
internal fun SettingTopThemeSection(
    onClickAppTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        SettingTopTitleItem(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.setting_top_theme,
        )

        SettingTextItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_theme_app,
            description = R.string.setting_top_theme_app_description,
            onClick = onClickAppTheme,
        )
    }
}
