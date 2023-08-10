package caios.android.kanade.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.component.LibraryTopBarScrollBehavior
import caios.android.kanade.core.design.component.rememberLibraryTopBarScrollState
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.music.LastFmService
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.core.ui.controller.AppController
import caios.android.kanade.core.ui.dialog.LoadingDialog
import caios.android.kanade.core.ui.dialog.PermissionDialog
import caios.android.kanade.feature.album.detail.navigateToAlbumDetail
import caios.android.kanade.feature.artist.detail.navigateToArtistDetail
import caios.android.kanade.feature.download.input.navigateToDownloadInput
import caios.android.kanade.feature.information.about.navigateToAbout
import caios.android.kanade.feature.lyrics.top.navigateToLyricsTop
import caios.android.kanade.feature.playlist.add.navigateToAddToPlaylist
import caios.android.kanade.feature.playlist.detail.navigateToPlaylistDetail
import caios.android.kanade.feature.search.scan.navigateToScanMedia
import caios.android.kanade.feature.setting.top.navigateToSettingTop
import caios.android.kanade.navigation.KanadeNavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Suppress("SwallowedException")
@Composable
fun KanadeApp(
    musicViewModel: MusicViewModel,
    userData: UserData?,
    appState: KanadeAppState,
    modifier: Modifier = Modifier,
) {
    KanadeBackground(Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val activity = (LocalContext.current as Activity)
        val drawerState = rememberDrawerState(DrawerValue.Closed)

        val safLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocumentTree(),
            onResult = { uri ->
                uri?.let {
                    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

                    activity.contentResolver.takePersistableUriPermission(it, flag)
                    appState.navController.navigateToScanMedia(it)
                }
            },
        )

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                KanadeDrawer(
                    state = drawerState,
                    currentSong = musicViewModel.uiState.song,
                    currentDestination = appState.currentDestination,
                    onClickItem = appState::navigateToLibrary,
                    navigateToQueue = { appState.navigateToQueue(activity) },
                    navigateToMediaScan = { safLauncher.launch(null) },
                    navigateToDownloadInput = { appState.navController.navigateToDownloadInput() },
                    navigateToSetting = { appState.navController.navigateToSettingTop() },
                    navigateToAbout = { appState.navController.navigateToAbout() },
                    navigateToSupport = { },
                )
            },
            gesturesEnabled = (appState.currentLibraryDestination != null),
        ) {
            var isSearchActive by remember { mutableStateOf(false) }
            val isShouldHideBottomController = isSearchActive || appState.currentLibraryDestination == null

            var topBarHeight by remember { mutableFloatStateOf(0f) }
            var bottomBarHeight by remember { mutableFloatStateOf(0f) }
            var bottomSheetHeight by remember { mutableFloatStateOf(0f) }
            var bottomSheetOffsetRate by remember { mutableFloatStateOf(-1f) }

            val scope = rememberCoroutineScope()
            val scaffoldState = rememberBottomSheetScaffoldState()
            val topAppBarState = rememberLibraryTopBarScrollState()
            val scrollBehavior = LibraryTopBarScrollBehavior(
                state = topAppBarState,
                topBarHeight = topBarHeight,
            )

            val topBarAlpha by animateFloatAsState(
                targetValue = if (appState.currentLibraryDestination == null) 0f else 1f,
                label = "topBarAlpha",
                animationSpec = tween(200),
            )

            val bottomSheetPeekHeight by animateDpAsState(
                targetValue = if (isShouldHideBottomController) {
                    val passing = WindowInsets.navigationBars.asPaddingValues()
                    72.dp + passing.calculateBottomPadding() + passing.calculateTopPadding()
                } else {
                    72.dp + with(density) { bottomBarHeight.toDp() }
                },
                label = "bottomSheetPeekHeight",
                animationSpec = tween(
                    durationMillis = 200,
                    easing = NavigateAnimation.decelerateEasing,
                ),
            )

            val bottomBarOffset by animateDpAsState(
                targetValue = with(density) {
                    bottomBarHeight.toDp()
                } * if (isShouldHideBottomController) 1f else (1f - bottomSheetOffsetRate),
                label = "bottomBarOffset",
                animationSpec = tween(
                    durationMillis = 200,
                    easing = NavigateAnimation.decelerateEasing,
                ),
            )

            val toolbarOffset = with(density) { if (!isSearchActive) scrollBehavior.state.yOffset.toDp() else 0.dp }

            bottomSheetOffsetRate = try {
                val offset = scaffoldState.bottomSheetState.requireOffset()
                val defaultHeight = bottomSheetHeight - bottomBarHeight - with(density) { 72.dp.toPx() }

                (offset / defaultHeight).coerceIn(0f, 1f)
            } catch (e: Throwable) {
                1f
            }

            LaunchedEffect(appState.currentLibraryDestination) {
                animate(
                    initialValue = scrollBehavior.state.yOffset,
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = NavigateAnimation.decelerateEasing,
                    ),
                ) { value, _ ->
                    scrollBehavior.state.yOffset = value
                }
            }

            LaunchedEffect(isSearchActive) {
                scrollBehavior.state.yOffset = 0f
            }

            LaunchedEffect(musicViewModel.uiState.isExpandedController) {
                if (musicViewModel.uiState.isExpandedController) {
                    scaffoldState.bottomSheetState.expand()
                } else {
                    scaffoldState.bottomSheetState.partialExpand()
                }
            }

            LaunchedEffect(musicViewModel.uiState.isReadyToFmService) {
                if (musicViewModel.uiState.isReadyToFmService) {
                    activity.startService(Intent(activity, LastFmService::class.java))
                }
            }

            Scaffold(
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    KanadeBottomBar(
                        modifier = Modifier
                            .onGloballyPositioned { bottomBarHeight = it.size.height.toFloat() }
                            .offset(y = bottomBarOffset)
                            .alpha(bottomSheetOffsetRate),
                        destination = appState.libraryDestinations.toImmutableList(),
                        onNavigateToDestination = appState::navigateToLibrary,
                        currentDestination = appState.currentDestination,
                    )
                },
            ) { paddingValues ->
                RequestPermissions(musicViewModel::fetch)

                val padding = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = bottomSheetPeekHeight,
                )

                BottomSheetScaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { bottomSheetHeight = it.size.height.toFloat() }
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal,
                            ),
                        ),
                    scaffoldState = scaffoldState,
                    sheetShape = RectangleShape,
                    sheetDragHandle = {},
                    sheetContent = {
                        AppController(
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                            uiState = musicViewModel.uiState,
                            windowSize = appState.windowSize,
                            offsetRate = bottomSheetOffsetRate,
                            onControllerEvent = musicViewModel::playerEvent,
                            onClickBottomController = {
                                scope.launch {
                                    scaffoldState.bottomSheetState.expand()
                                }
                            },
                            onClickCloseExpanded = {
                                scope.launch {
                                    scaffoldState.bottomSheetState.partialExpand()
                                }
                            },
                            onClickFavorite = {
                                scope.launch {
                                    musicViewModel.onFavorite(it)
                                }
                            },
                            onFetchFavorite = {
                                musicViewModel.fetchFavorite(it)
                            },
                            navigateToAddToPlaylist = {
                                appState.navController.navigateToAddToPlaylist(listOf(it))
                            },
                            navigateToArtist = {
                                appState.navController.navigateToArtistDetail(it)
                            },
                            navigateToAlbum = {
                                appState.navController.navigateToAlbumDetail(it)
                            },
                            navigateToLyrics = {
                                appState.navController.navigateToLyricsTop(it)
                            },
                            navigateToSearch = {
                                isSearchActive = true
                            },
                            navigateToSleepTimer = { },
                            navigateToQueue = { appState.navigateToQueue(activity) },
                            navigateToKaraoke = { },
                        )
                    },
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    sheetPeekHeight = bottomSheetPeekHeight,
                ) {
                    Box {
                        if (musicViewModel.uiState.isAnalyzing) {
                            LoadingDialog(R.string.common_analyzing)
                        }

                        if (topBarAlpha > 0f) {
                            KanadeTopBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned {
                                        if (topBarHeight == 0f) topBarHeight = it.size.height.toFloat()
                                    }
                                    .zIndex(if (appState.currentLibraryDestination == null) 0f else 1f)
                                    .alpha(topBarAlpha),
                                active = isSearchActive,
                                yOffset = toolbarOffset,
                                onChangeActive = { isSearchActive = it },
                                onClickDrawerMenu = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                                navigateToArtistDetail = {
                                    appState.navController.navigateToArtistDetail(it)
                                },
                                navigateToAlbumDetail = {
                                    appState.navController.navigateToAlbumDetail(it)
                                },
                                navigateToPlaylistDetail = {
                                    appState.navController.navigateToPlaylistDetail(it)
                                },
                                navigateToSongMenu = { appState.showSongMenuDialog(activity, it) },
                                navigateToArtistMenu = { appState.showArtistMenuDialog(activity, it) },
                                navigateToAlbumMenu = { appState.showAlbumMenuDialog(activity, it) },
                                navigateToPlaylistMenu = { appState.showPlaylistMenuDialog(activity, it) },
                            )
                        }

                        KanadeNavHost(
                            musicViewModel = musicViewModel,
                            appState = appState,
                            userData = userData,
                            libraryTopBarHeight = with(density) { topBarHeight.toDp() },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissions(onGranted: () -> Unit) {
    var isPermissionRequested by remember { mutableStateOf(false) }
    var isShowPermissionDialog by remember { mutableStateOf(true) }

    val notifyPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.POST_NOTIFICATIONS else null
    val storagePermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_AUDIO else android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    val locationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) android.Manifest.permission.ACCESS_FINE_LOCATION else null

    val permissionList = listOfNotNull(storagePermission, notifyPermission, locationPermission)
    val permissionsState = rememberMultiplePermissionsState(permissionList) {
        isPermissionRequested = true
    }

    if (permissionsState.permissions[0].status is PermissionStatus.Granted) {
        if (isPermissionRequested) {
            isPermissionRequested = false
            onGranted.invoke()
        }
        return
    }

    if (permissionsState.shouldShowRationale || isPermissionRequested) {
        PermissionDialog(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            onDismiss = { isShowPermissionDialog = false },
        )
    } else {
        LaunchedEffect(permissionsState) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
}
