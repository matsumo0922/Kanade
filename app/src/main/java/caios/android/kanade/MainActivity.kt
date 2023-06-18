package caios.android.kanade

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import caios.android.kanade.core.design.theme.KanadeTheme
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.ui.KanadeApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val musicViewModel by viewModels<MusicViewModel>()

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
            val systemUiController = rememberSystemUiController()
            val shouldUseDarkTheme = shouldUseDarkTheme(screenState)

            DisposableEffect(systemUiController, shouldUseDarkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !shouldUseDarkTheme
                onDispose {}
            }

            AsyncLoadContents(screenState) { userData ->
                KanadeTheme(
                    shouldUseDarkTheme = shouldUseDarkTheme,
                    enableDynamicTheme = shouldUseDynamicColor(screenState),
                ) {
                    KanadeApp(
                        musicViewModel = musicViewModel,
                        userData = userData,
                        windowSize = calculateWindowSizeClass(this),
                    )
                }
            }
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
            is ScreenState.Idle -> screenState.data.useDynamicColor
            else -> false
        }
    }
}
