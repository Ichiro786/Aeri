package com.ardia.design

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val LocalAeriDarkTheme = staticCompositionLocalOf { false }
val LocalReduceMotion = staticCompositionLocalOf { false }
val LocalReduceTransparency = staticCompositionLocalOf { false }
val LocalIncreasedContrast = staticCompositionLocalOf { false }

object AeriThemeState {
    val isDark: Boolean
        @Composable @ReadOnlyComposable get() = LocalAeriDarkTheme.current

    val reduceMotion: Boolean
        @Composable @ReadOnlyComposable get() = LocalReduceMotion.current

    val look: ProfileLook
        @Composable @ReadOnlyComposable get() = LocalProfileLook.current

    @Composable
    @ReadOnlyComposable
    fun motion(full: AeriMotion, reduced: AeriMotion = Motion.reducedFade): AeriMotion = if (LocalReduceMotion.current) reduced else full
}

object SystemAccessibility {
    fun reduceMotion(context: Context): Boolean =
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f

    fun increasedContrast(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) return false
        val manager = context.getSystemService(UiModeManager::class.java) ?: return false
        return manager.contrast >= 0.5f
    }
}

@Composable
fun AeriTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    look: ProfileLook = ProfileLook.plum,
    reduceMotion: Boolean? = null,
    reduceTransparency: Boolean = false,
    increasedContrast: Boolean? = null,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val systemReduceMotion = remember(context) { SystemAccessibility.reduceMotion(context) }
    val systemContrast = remember(context) { SystemAccessibility.increasedContrast(context) }
    val colors = remember(darkTheme, look) { AeriMaterial.colorScheme(darkTheme, look) }
    CompositionLocalProvider(
        LocalAeriDarkTheme provides darkTheme,
        LocalProfileLook provides look,
        LocalReduceMotion provides (reduceMotion ?: systemReduceMotion),
        LocalReduceTransparency provides reduceTransparency,
        LocalIncreasedContrast provides (increasedContrast ?: systemContrast),
    ) {
        MaterialTheme(colorScheme = colors, typography = AeriMaterial.typography) {
            CompositionLocalProvider(LocalContentColor provides SystemPalette.label.resolve(darkTheme), content = content)
        }
    }
}

object AeriMaterial {
    val typography = Typography(
        displayLarge = AeriType.largeTitle,
        displayMedium = AeriType.largeTitle,
        displaySmall = AeriType.title,
        headlineLarge = AeriType.title,
        headlineMedium = AeriType.title2,
        headlineSmall = AeriType.title3,
        titleLarge = AeriType.title2.bold,
        titleMedium = AeriType.headline,
        titleSmall = AeriType.subheadline.semibold,
        bodyLarge = AeriType.body,
        bodyMedium = AeriType.subheadline,
        bodySmall = AeriType.footnote,
        labelLarge = AeriType.subheadline.semibold,
        labelMedium = AeriType.caption.semibold,
        labelSmall = AeriType.caption2,
    )

    fun colorScheme(dark: Boolean, look: ProfileLook = ProfileLook.plum): ColorScheme {
        val accent = Palette.formAccent.resolve(dark)
        val label = SystemPalette.label.resolve(dark)
        val secondaryLabel = SystemPalette.secondaryLabel.resolve(dark)
        val background = look.surface.resolve(dark)
        val grouped = look.surfaceLow.resolve(dark)
        val card = SystemPalette.secondaryGroupedBackground.resolve(dark)
        val popover = Palette.panel.resolve(dark)
        val separator = SystemPalette.separator.resolve(dark)
        val error = SystemPalette.red.resolve(dark)
        return if (dark) {
            darkColorScheme(
                primary = accent, onPrimary = Color.White, secondary = accent, onSecondary = Color.White,
                background = background, onBackground = label, surface = background, onSurface = label,
                surfaceVariant = card, onSurfaceVariant = secondaryLabel, surfaceContainer = card, surfaceContainerHigh = popover,
                surfaceContainerLow = grouped, outline = separator, outlineVariant = separator, error = error, onError = Color.White,
            )
        } else {
            lightColorScheme(
                primary = accent, onPrimary = Color.White, secondary = accent, onSecondary = Color.White,
                background = background, onBackground = label, surface = background, onSurface = label,
                surfaceVariant = card, onSurfaceVariant = secondaryLabel, surfaceContainer = card, surfaceContainerHigh = popover,
                surfaceContainerLow = grouped, outline = separator, outlineVariant = separator, error = error, onError = Color.White,
            )
        }
    }
}
