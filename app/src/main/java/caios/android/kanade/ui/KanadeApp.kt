package caios.android.kanade.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.audiofx.AudioEffect
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
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
import caios.android.kanade.core.common.network.util.ToastUtil
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
import caios.android.kanade.feature.album.detail.navigateToAlbumDetail
import caios.android.kanade.feature.artist.detail.navigateToArtistDetail
import caios.android.kanade.feature.download.input.navigateToDownloadInput
import caios.android.kanade.feature.information.about.navigateToAbout
import caios.android.kanade.feature.information.song.navigateToSongInformation
import caios.android.kanade.feature.lyrics.top.navigateToLyricsTop
import caios.android.kanade.feature.playlist.add.navigateToAddToPlaylist
import caios.android.kanade.feature.playlist.detail.navigateToPlaylistDetail
import caios.android.kanade.feature.search.scan.navigateToScanMedia
import caios.android.kanade.feature.setting.top.navigateToSettingTop
import caios.android.kanade.feature.welcome.WelcomeNavHost
import caios.android.kanade.navigation.KanadeNavHost
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun KanadeApp(
    musicViewModel: MusicViewModel,
    isAgreedTeams: Boolean,
    isAllowedPermission: Boolean,
    userData: UserData,
    appState: KanadeAppState,
    modifier: Modifier = Modifier,
) {
    val activity = (LocalContext.current as Activity)
    var isShowWelcomeScreen by remember { mutableStateOf(!isAgreedTeams || !isAllowedPermission) }

    KanadeBackground(modifier) {
        AnimatedContent(
            targetState = isShowWelcomeScreen && (!isAgreedTeams || !isAllowedPermission),
            label = "isShowWelcomeScreen",
        ) {
            if (it) {
                WelcomeNavHost(
                    modifier = Modifier.fillMaxSize(),
                    isAgreedTeams = isAgreedTeams,
                    isAllowedPermission = isAllowedPermission,
                    navigateToBillingPlus = { appState.showBillingPlusDialog(activity) },
                    onComplete = { isShowWelcomeScreen = false },
                )
            } else {
                IdleScreen(
                    modifier = Modifier.fillMaxSize(),
                    musicViewModel = musicViewModel,
                    userData = userData,
                    appState = appState,
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun IdleScreen(
    musicViewModel: MusicViewModel,
    userData: UserData,
    appState: KanadeAppState,
    modifier: Modifier = Modifier,
) {
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
                userData = userData,
                currentSong = musicViewModel.uiState.song,
                currentDestination = appState.currentDestination,
                onClickItem = appState::navigateToLibrary,
                navigateToQueue = { appState.navigateToQueue(activity) },
                navigateToMediaScan = { safLauncher.launch(null) },
                navigateToSetting = { appState.navController.navigateToSettingTop() },
                navigateToAbout = { appState.navController.navigateToAbout() },
                navigateToSupport = { },
                navigateToBillingPlus = { appState.showBillingPlusDialog(activity) },
                navigateToDownloadInput = {
                    if (musicViewModel.uiState.isEnableYoutubeDL) {
                        appState.navController.navigateToDownloadInput()
                    } else {
                        ToastUtil.show(activity, R.string.error_developing_feature)
                    }
                },
            )
        },
        gesturesEnabled = (appState.currentLibraryDestination != null),
    ) {
        var isSearchActive by remember { mutableStateOf(false) }
        val isShouldHideBottomController = isSearchActive || appState.currentLibraryDestination == null

        var topBarHeight by remember { mutableFloatStateOf(0f) }
        var bottomBarHeight by remember { mutableFloatStateOf(0f) }
        var bottomSheetHeight by remember { mutableFloatStateOf(0f) }
        var bottomSheetOffset by remember { mutableFloatStateOf(0f) }
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

        bottomSheetOffset = runCatching { scaffoldState.bottomSheetState.requireOffset() }.getOrDefault(0f)
        bottomSheetOffsetRate = density.run {
            (bottomSheetOffset / (bottomSheetHeight - bottomBarHeight - 72.dp.toPx())).coerceIn(0f, 1f)
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
                scaffoldState.bottomSheetState.collapse()
            }
        }

        LaunchedEffect(musicViewModel.uiState.isDisplayedPlusDialog) {
            if (musicViewModel.uiState.isDisplayedPlusDialog) {
                appState.showBillingPlusDialog(activity)
                musicViewModel.setPlusDialogDisplayed(false)
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
                        .fillMaxWidth()
                        .onGloballyPositioned { bottomBarHeight = it.size.height.toFloat() }
                        .offset(y = bottomBarOffset)
                        .alpha(bottomSheetOffsetRate),
                    destination = appState.libraryDestinations.toImmutableList(),
                    onNavigateToDestination = appState::navigateToLibrary,
                    currentDestination = appState.currentDestination,
                )
            },
        ) {
            BottomSheetScaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { bottomSheetHeight = it.size.height.toFloat() }
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
                scaffoldState = scaffoldState,
                sheetShape = RectangleShape,
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                sheetPeekHeight = bottomSheetPeekHeight,
                sheetContent = {
                    AppController(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface),
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
                                scaffoldState.bottomSheetState.collapse()
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
                        navigateToQueue = { appState.navigateToQueue(activity) },
                        navigateToSongInfo = { appState.navController.navigateToSongInformation(it) },
                        navigateToEqualizer = {
                            activity.startActivity(
                                Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL).apply {
                                    putExtra(AudioEffect.EXTRA_PACKAGE_NAME, activity.packageName)
                                    putExtra(AudioEffect.EXTRA_AUDIO_SESSION, 0)
                                    putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                                },
                            )
                        },
                    )
                },
            ) {
                Box {
                    if (musicViewModel.uiState.isAnalyzing) {
                        LoadingDialog(R.string.common_analyzing)
                    }

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

                    KanadeNavHost(
                        musicViewModel = musicViewModel,
                        appState = appState,
                        userData = userData,
                        libraryTopBarHeight = with(density) { topBarHeight.toDp() },
                    )

                    BackHandler(scaffoldState.bottomSheetState.currentValue == BottomSheetValue.Expanded) {
                        scope.launch {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            }
        }
    }
}
