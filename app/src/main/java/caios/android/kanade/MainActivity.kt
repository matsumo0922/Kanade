package caios.android.kanade

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import caios.android.kanade.core.design.theme.KanadeTheme
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.ui.KanadeApp
import caios.android.kanade.ui.rememberKanadeAppState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val musicViewModel by viewModels<MusicViewModel>()

    @Inject
    lateinit var musicController: MusicController

    @Inject
    lateinit var musicRepository: MusicRepository

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var screenState: ScreenState<UserData> by mutableStateOf(ScreenState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.onEach { screenState = it }.collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (screenState) {
                is ScreenState.Loading -> true
                else -> false
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val windowSize = calculateWindowSizeClass(this)
            val systemUiController = rememberSystemUiController()
            val shouldUseDarkTheme = shouldUseDarkTheme(screenState)

            DisposableEffect(systemUiController, shouldUseDarkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !shouldUseDarkTheme
                onDispose {}
            }

            AsyncLoadContents(
                modifier = Modifier.fillMaxSize(),
                screenState = screenState,
            ) { userData ->
                val appState = rememberKanadeAppState(windowSize, musicViewModel, userData)

                KanadeTheme(
                    themeColorConfig = userData.themeColorConfig,
                    shouldUseDarkTheme = shouldUseDarkTheme,
                    enableDynamicTheme = shouldUseDynamicColor(screenState),
                ) {
                    KanadeApp(
                        musicViewModel = musicViewModel,
                        userData = userData,
                        appState = appState,
                    )
                }
            }
        }

        if (intent?.extras?.getBoolean("notify") == true) {
            musicViewModel.setControllerState(true)
            intent?.extras?.remove("notify")
        }

        lifecycleScope.launch {
            musicViewModel.fetch()
            musicController.initialize()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val screenState = viewModel.screenState.value

        if (screenState is ScreenState.Idle && screenState.data.isStopWhenTaskkill) {
            musicViewModel.playerEvent(PlayerEvent.Pause)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.extras?.getBoolean("notify") == true) {
            musicViewModel.setControllerState(true)
        }
    }

    @Composable
    private fun shouldUseDarkTheme(screenState: ScreenState<UserData>): Boolean {
        return when (screenState) {
            is ScreenState.Idle -> when (screenState.data.themeConfig) {
                ThemeConfig.Light -> false
                ThemeConfig.Dark -> true
                else -> isSystemInDarkTheme()
            }
            else -> isSystemInDarkTheme()
        }
    }

    @Composable
    private fun shouldUseDynamicColor(screenState: ScreenState<UserData>): Boolean {
        return when (screenState) {
            is ScreenState.Idle -> screenState.data.isDynamicColor
            else -> false
        }
    }
}
