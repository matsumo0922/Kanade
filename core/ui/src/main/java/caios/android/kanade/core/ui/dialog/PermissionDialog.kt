package caios.android.kanade.core.ui.dialog

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import caios.android.kanade.core.design.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
) {
    val context = LocalContext.current

    val notifyPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.POST_NOTIFICATIONS else null
    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_AUDIO else android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    val permissionList = listOfNotNull(storagePermission, notifyPermission)
    val permissionsState = rememberMultiplePermissionsState(permissionList)

    if (permissionsState.permissions[0].status is PermissionStatus.Granted) {
        onDismiss.invoke()
        return
    }

    PermissionDialog(
        modifier = modifier,
        onConfirm = {
            if (permissionsState.revokedPermissions.isNotEmpty()) {
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null),
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    },
                )
            } else {
                permissionsState.launchMultiplePermissionRequest()
            }
        },
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PermissionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        modifier = modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.permission_request_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                Divider()

                Text(
                    modifier = Modifier.padding(vertical = 16.dp),
                    text = stringResource(R.string.permission_request_message),
                    style = MaterialTheme.typography.bodyMedium,
                )

                StorageSection(Modifier.padding(8.dp))

                NotifySection(Modifier.padding(8.dp))
            }
        },
        confirmButton = {
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                TextButton(
                    modifier = Modifier.defaultMinSize(1.dp, 1.dp),
                    onClick = onConfirm,
                    contentPadding = PaddingValues(8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.common_allow),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        },
        dismissButton = {
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                TextButton(
                    modifier = Modifier.defaultMinSize(1.dp, 1.dp),
                    onClick = onDismiss,
                    contentPadding = PaddingValues(8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.common_denied),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        },
    )
}

@Composable
private fun StorageSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            imageVector = Icons.Default.Save,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.permission_request_storage),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = stringResource(R.string.permission_request_storage_why),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun NotifySection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.permission_request_notification),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = stringResource(R.string.permission_request_notification_why),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PermissionDialog(
        onConfirm = {},
        onDismiss = {},
    )
}