package caios.android.kanade.feature.setting.top.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.feature.setting.top.settings.SettingSwitchItem
import caios.android.kanade.feature.setting.top.settings.SettingTextItem

@Composable
internal fun SettingTopPlayingSection(
    userData: UserData,
    onClickEqualizer: () -> Unit,
    onClickDynamicNormalizer: (Boolean) -> Unit,
    onClickOneStepBack: (Boolean) -> Unit,
    onClickKeepAudioFocus: (Boolean) -> Unit,
    onClickStopWhenTaskkill: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        SettingTopTitleItem(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.setting_top_playing,
        )

        SettingTextItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_playing_equalizer,
            description = R.string.setting_top_playing_equalizer_description,
            onClick = onClickEqualizer,
        )

        SettingSwitchItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_playing_dynamic_normalizer,
            description = R.string.setting_top_playing_dynamic_normalizer_description,
            value = userData.isDynamicNormalizer,
            onValueChanged = onClickDynamicNormalizer,
        )

        SettingSwitchItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_playing_one_step_back,
            description = R.string.setting_top_playing_one_step_back_description,
            value = userData.isOneStepBack,
            onValueChanged = onClickOneStepBack,
        )

        SettingSwitchItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_playing_keep_audio_focus,
            description = R.string.setting_top_playing_keep_audio_focus_description,
            value = userData.isKeepAudioFocus,
            onValueChanged = onClickKeepAudioFocus,
        )

        SettingSwitchItem(
            modifier = Modifier.fillMaxWidth(),
            title = R.string.setting_top_playing_stop_when_taskkill,
            description = R.string.setting_top_playing_stop_when_taskkill_description,
            value = userData.isStopWhenTaskkill,
            onValueChanged = onClickStopWhenTaskkill,
        )
    }
}
