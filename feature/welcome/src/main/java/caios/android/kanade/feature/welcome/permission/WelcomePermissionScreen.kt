package caios.android.kanade.feature.welcome.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.design.theme.center
import caios.android.kanade.feature.welcome.WelcomeIndicatorItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun WelcomePermissionScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isPermissionRequested by remember { mutableStateOf(false) }

    val notifyPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.POST_NOTIFICATIONS else null
    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_AUDIO else android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    val permissionList = listOfNotNull(storagePermission, notifyPermission)
    val permissionsState = rememberMultiplePermissionsState(permissionList) {
        isPermissionRequested = true
    }

    val isGrantedStorage = permissionsState.permissions[0].status is PermissionStatus.Granted
    val isGrantedNotify = permissionsState.permissions[1].status is PermissionStatus.Granted

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.padding(
                top = 8.dp,
                start = 8.dp,
                end = 8.dp,
            ),
            painter = painterResource(R.drawable.vec_welcome_permission),
            contentDescription = null,
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(if (isGrantedStorage) R.string.welcome_permission_ready_title else R.string.welcome_permission_title),
            style = MaterialTheme.typography.displaySmall.bold(),
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(if (isGrantedStorage) R.string.welcome_permission_ready_message else R.string.welcome_permission_message),
            style = MaterialTheme.typography.bodySmall.center(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Column(
            modifier = Modifier.padding(top = 48.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            WelcomePermissionItem(
                isAllowed = isGrantedStorage,
                isOptional = false,
                title = stringResource(R.string.welcome_permission_storage),
                message = stringResource(R.string.welcome_permission_storage_message),
            )

            WelcomePermissionItem(
                isAllowed = isGrantedNotify,
                isOptional = true,
                title = stringResource(R.string.welcome_permission_notification),
                message = stringResource(R.string.welcome_permission_notification_message),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        WelcomeIndicatorItem(
            modifier = Modifier.padding(bottom = 24.dp),
            max = 3,
            step = 3,
        )

        if (isGrantedStorage) {
            Button(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50),
                onClick = { navigateToHome.invoke() },
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.welcome_permission_complete_button),
                )
            }
        } else {
            Button(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50),
                onClick = {
                    if (permissionsState.shouldShowRationale || isPermissionRequested) {
                        startAppSettings(context)
                    } else {
                        scope.launch {
                            permissionsState.launchMultiplePermissionRequest()
                        }
                    }
                },
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.welcome_permission_allow_button),
                )
            }
        }
    }
}

@Composable
private fun WelcomePermissionItem(
    isAllowed: Boolean,
    isOptional: Boolean,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        WelcomePermissionLabel(
            modifier = Modifier.width(64.dp),
            isAllowed = isAllowed,
            isOptional = isOptional,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun WelcomePermissionLabel(
    isAllowed: Boolean,
    isOptional: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = when {
        isAllowed -> MaterialTheme.colorScheme.primary
        isOptional -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(50),
            ),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            text = when {
                isAllowed -> stringResource(R.string.welcome_permission_allowed)
                isOptional -> stringResource(R.string.welcome_permission_optional)
                else -> stringResource(R.string.welcome_permission_denied)
            },
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
    }
}

private fun startAppSettings(context: Context) {
    context.startActivity(
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null),
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        },
    )
}

@Preview
@Composable
private fun WelcomePermissionScreenPreview() {
    WelcomePermissionScreen(
        modifier = Modifier.fillMaxSize(),
        navigateToHome = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun WelcomePermissionItemPreview() {
    WelcomePermissionItem(
        isAllowed = true,
        isOptional = false,
        title = "Camera",
        message = "Allow access to your camera to take photos and videos",
    )
}
