package caios.android.kanade.ui

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.ui.dialog.PermissionDialog
import caios.android.kanade.navigation.KanadeNavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Suppress("ModifierMissing")
@Composable
fun KanadeApp(
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier,
    appState: KanadeAppState = rememberKanadeAppState(windowSize),
) {
    KanadeBackground {
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                KanadeBottomBar(
                    destination = appState.libraryDestinations,
                    onNavigateToDestination = appState::navigateToLibrary,
                    currentDestination = appState.currentDestination,
                )
            },
        ) { padding ->

            RequestPermissions()

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                KanadeNavHost(appState)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissions() {
    var isPermissionRequested by remember { mutableStateOf(false) }
    var isShowPermissionDialog by remember { mutableStateOf(true) }

    val notifyPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.POST_NOTIFICATIONS else null
    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_AUDIO else android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    val permissionList = listOfNotNull(storagePermission, notifyPermission)
    val permissionsState = rememberMultiplePermissionsState(permissionList) {
        isPermissionRequested = true
    }

    if (permissionsState.permissions[0].status is PermissionStatus.Granted) return

    if (permissionsState.shouldShowRationale || isPermissionRequested) {
        PermissionDialog(onDismiss = { isShowPermissionDialog = false })
    } else {
        LaunchedEffect(permissionsState) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
}
