package caios.android.kanade.core.design.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.theme.color.DarkBlueColorScheme
import caios.android.kanade.core.design.theme.color.DarkBrownColorScheme
import caios.android.kanade.core.design.theme.color.DarkGreenColorScheme
import caios.android.kanade.core.design.theme.color.DarkPinkColorScheme
import caios.android.kanade.core.design.theme.color.DarkPurpleColorScheme
import caios.android.kanade.core.design.theme.color.LightBlueColorScheme
import caios.android.kanade.core.design.theme.color.LightBrownColorScheme
import caios.android.kanade.core.design.theme.color.LightGreenColorScheme
import caios.android.kanade.core.design.theme.color.LightPinckColorScheme
import caios.android.kanade.core.design.theme.color.LightPurpleColorScheme
import caios.android.kanade.core.model.ThemeColorConfig

val LightDefaultColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Orange40,
    onSecondary = Color.White,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = Blue40,
    onTertiary = Color.White,
    tertiaryContainer = Blue90,
    onTertiaryContainer = Blue10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkPurpleGray99,
    onBackground = DarkPurpleGray10,
    surface = DarkPurpleGray99,
    onSurface = DarkPurpleGray10,
    surfaceVariant = PurpleGray90,
    onSurfaceVariant = PurpleGray30,
    inverseSurface = DarkPurpleGray20,
    inverseOnSurface = DarkPurpleGray95,
    outline = PurpleGray50,
)

val DarkDefaultColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = Blue80,
    onTertiary = Blue20,
    tertiaryContainer = Blue30,
    onTertiaryContainer = Blue90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkPurpleGray10,
    onBackground = DarkPurpleGray90,
    surface = DarkPurpleGray10,
    onSurface = DarkPurpleGray90,
    surfaceVariant = PurpleGray30,
    onSurfaceVariant = PurpleGray80,
    inverseSurface = DarkPurpleGray90,
    inverseOnSurface = DarkPurpleGray10,
    outline = PurpleGray60,
)

@Composable
fun KanadeTheme(
    themeColorConfig: ThemeColorConfig = ThemeColorConfig.Default,
    shouldUseDarkTheme: Boolean = isSystemInDarkTheme(),
    enableDynamicTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = when {
        enableDynamicTheme && supportsDynamicTheming() -> if (shouldUseDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        else -> when (themeColorConfig) {
            ThemeColorConfig.Blue -> if (shouldUseDarkTheme) DarkBlueColorScheme else LightBlueColorScheme
            ThemeColorConfig.Brown -> if (shouldUseDarkTheme) DarkBrownColorScheme else LightBrownColorScheme
            ThemeColorConfig.Green -> if (shouldUseDarkTheme) DarkGreenColorScheme else LightGreenColorScheme
            ThemeColorConfig.Purple -> if (shouldUseDarkTheme) DarkPurpleColorScheme else LightPurpleColorScheme
            ThemeColorConfig.Pink -> if (shouldUseDarkTheme) DarkPinkColorScheme else LightPinckColorScheme
            else -> if (shouldUseDarkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
        }
    }

    val backgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )

    val tintTheme = when {
        enableDynamicTheme && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }

    CompositionLocalProvider(
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = KanadeTypography,
            content = content,
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
private fun supportsDynamicTheming(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}
