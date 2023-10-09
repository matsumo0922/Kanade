package caios.android.kanade.feature.setting.ytmusic

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.center
import caios.android.kanade.core.model.entity.YTMusicOAuthCode
import caios.android.kanade.core.ui.AsyncLoadContentsWithoutAnimation
import kotlinx.coroutines.launch

@Composable
internal fun YTMusicLoginRoute(
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: YTMusicLoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContentsWithoutAnimation(
        modifier = modifier,
        screenState = screenState,
        retryAction = { terminate.invoke() },
    ) { uiState ->
        YTMusicLoginDialog(
            modifier = Modifier.fillMaxWidth(),
            isReadyToAuth = uiState.isReadyToAuth,
            oauthCode = uiState.oauthCode,
            onClickOpenBrowser = {
                context.startActivity(Intent(Intent.ACTION_VIEW, it.toUri()))
                viewModel.readyToAuth()
            },
            onClickDone = {
                scope.launch {
                    if (viewModel.getOAuthToken(uiState.oauthCode)) {
                        ToastUtil.show(context, R.string.setting_ytmusic_login_toast_success)
                        terminate.invoke()
                    } else {
                        ToastUtil.show(context, R.string.setting_ytmusic_login_toast_failure)
                    }
                }
            },
            onTerminate = terminate,
        )
    }
}

@Composable
private fun YTMusicLoginDialog(
    isReadyToAuth: Boolean,
    oauthCode: YTMusicOAuthCode,
    onClickOpenBrowser: (String) -> Unit,
    onClickDone: () -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title: String
    val description: String
    val buttonText: String

    if (!isReadyToAuth) {
        title = stringResource(R.string.setting_ytmusic_login_title1)
        description = stringResource(R.string.setting_ytmusic_login_description1)
        buttonText = stringResource(R.string.setting_ytmusic_login_button1)
    } else {
        title = stringResource(R.string.setting_ytmusic_login_title2)
        description = stringResource(R.string.setting_ytmusic_login_description2)
        buttonText = stringResource(R.string.setting_ytmusic_login_button2)
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            text = oauthCode.userCode,
            style = MaterialTheme.typography.displaySmall.center(),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp),
                onClick = { onTerminate.invoke() },
            ) {
                Text(
                    text = stringResource(R.string.common_cancel),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Button(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp),
                onClick = {
                    if (!isReadyToAuth) {
                        onClickOpenBrowser.invoke("${oauthCode.verificationUrl}?user_code=${oauthCode.userCode}")
                    } else {
                        onClickDone.invoke()
                    }
                },
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
