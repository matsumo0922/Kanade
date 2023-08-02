package caios.android.kanade.feature.setting.theme

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.ui.AsyncLoadContents

@Composable
internal fun SettingThemeRoute(
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
            userData = it?.userData,
        )
    }
}

@Composable
private fun SettingThemeDialog(
    userData: UserData?,
    modifier: Modifier = Modifier,
) {

}
