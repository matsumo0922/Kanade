package caios.android.kanade.ui

import android.app.Activity
import android.os.Build
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
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.component.LibraryTopBar
import caios.android.kanade.core.design.component.LibraryTopBarScrollBehavior
import caios.android.kanade.core.design.component.rememberLibraryTopBarScrollState
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.core.ui.controller.AppController
import caios.android.kanade.core.ui.dialog.PermissionDialog
import caios.android.kanade.feature.album.detail.navigateToAlbumDetail
import caios.android.kanade.feature.artist.detail.navigateToArtistDetail
import caios.android.kanade.feature.menu.song.showSongMenuDialog
import caios.android.kanade.feature.playlist.add.navigateToAddToPlaylist
import caios.android.kanade.feature.queue.showQueueDialog
import caios.android.kanade.feature.search.navigateToSearch
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

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                KanadeDrawer(
                    currentDestination = appState.currentDestination,
                    onClickItem = appState::navigateToLibrary,
                )
            },
            gesturesEnabled = true,
        ) {
            var topBarHeight by remember { mutableFloatStateOf(0f) }
            var bottomBarHeight by remember { mutableFloatStateOf(0f) }
            var bottomSheetHeight by remember { mutableFloatStateOf(0f) }
            var bottomSheetOffsetRate by remember { mutableFloatStateOf(-1f) }

            val topBarAlpha by animateFloatAsState(
                targetValue = if (appState.currentLibraryDestination == null) 0f else 1f,
                label = "topBarAlpha",
                animationSpec = tween(240),
            )

            val bottomSheetPeekHeight by animateDpAsState(
                targetValue = if (appState.currentLibraryDestination == null) {
                    val passing = WindowInsets.navigationBars.asPaddingValues()
                    72.dp + passing.calculateBottomPadding() + passing.calculateTopPadding()
                } else {
                    72.dp + with(density) { bottomBarHeight.toDp() }
                },
                label = "bottomSheetPeekHeight",
                animationSpec = tween(
                    durationMillis = 240,
                    easing = NavigateAnimation.decelerateEasing,
                ),
            )

            val bottomBarOffset by animateDpAsState(
                targetValue = with(density) { bottomBarHeight.toDp() } * if (appState.currentLibraryDestination == null) 1f else (1f - bottomSheetOffsetRate),
                label = "bottomBarOffset",
                animationSpec = tween(
                    durationMillis = 240,
                    easing = NavigateAnimation.decelerateEasing,
                ),
            )

            val scope = rememberCoroutineScope()
            val scaffoldState = rememberBottomSheetScaffoldState()

            val topAppBarState = rememberLibraryTopBarScrollState()
            val scrollBehavior = LibraryTopBarScrollBehavior(
                state = topAppBarState,
                topBarHeight = topBarHeight,
            )

            bottomSheetOffsetRate = try {
                val offset = scaffoldState.bottomSheetState.requireOffset()
                val defaultHeight = bottomSheetHeight - bottomBarHeight - with(density) { 72.dp.toPx() }

                (offset / defaultHeight).coerceIn(0f, 1f)
            } catch (e: Throwable) {
                1f
            }

            LaunchedEffect(musicViewModel.uiState.isExpandedController) {
                scope.launch {
                    if (musicViewModel.uiState.isExpandedController) {
                        scaffoldState.bottomSheetState.expand()
                    } else {
                        scaffoldState.bottomSheetState.partialExpand()
                    }
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
            ) {
                RequestPermissions(musicViewModel::fetch)

                val padding = PaddingValues(
                    top = it.calculateTopPadding(),
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
                            onRequestLyrics = {
                                scope.launch {
                                    musicViewModel.fetchLyrics(it)
                                }
                            },
                            onFetchFavorite = {
                                musicViewModel.fetchFavorite(it)
                            },
                            navigateToLyrics = { },
                            navigateToSleepTimer = { },
                            navigateToQueue = {
                                activity.showQueueDialog(
                                    userData = userData,
                                    navigateToSongMenu = { song ->
                                        activity.showSongMenuDialog(
                                            musicViewModel = musicViewModel,
                                            userData = userData,
                                            song = song,
                                            navigateToAddToPlaylist = { songIds ->
                                                appState.navController.navigateToAddToPlaylist(songIds)
                                            },
                                            navigateToArtistDetail = { artistId ->
                                                appState.navController.navigateToArtistDetail(artistId)
                                            },
                                            navigateToAlbumDetail = { albumId ->
                                                appState.navController.navigateToAlbumDetail(albumId)
                                            },
                                        )
                                    },
                                )
                            },
                            navigateToKaraoke = { },
                        )
                    },
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    sheetPeekHeight = bottomSheetPeekHeight,
                ) {
                    Box {
                        if (topBarAlpha > 0f) {
                            LibraryTopBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { topBarHeight = it.size.height.toFloat() }
                                    .zIndex(if (appState.currentLibraryDestination == null) 0f else 1f)
                                    .alpha(topBarAlpha),
                                onClickMenu = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                                onClickSearch = {
                                    appState.navController.navigateToSearch()
                                },
                                scrollBehavior = scrollBehavior,
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

    val permissionList = listOfNotNull(storagePermission, notifyPermission)
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
