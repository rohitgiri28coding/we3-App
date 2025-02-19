package com.shopping.we3surveillancecalculator.ui.theme
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import org.project.we3.ui.theme.AppTypography
import org.project.we3.ui.theme.backgroundDark
import org.project.we3.ui.theme.backgroundDarkHighContrast
import org.project.we3.ui.theme.backgroundDarkMediumContrast
import org.project.we3.ui.theme.backgroundLight
import org.project.we3.ui.theme.backgroundLightHighContrast
import org.project.we3.ui.theme.backgroundLightMediumContrast
import org.project.we3.ui.theme.errorContainerDark
import org.project.we3.ui.theme.errorContainerDarkHighContrast
import org.project.we3.ui.theme.errorContainerDarkMediumContrast
import org.project.we3.ui.theme.errorContainerLight
import org.project.we3.ui.theme.errorContainerLightHighContrast
import org.project.we3.ui.theme.errorContainerLightMediumContrast
import org.project.we3.ui.theme.errorDark
import org.project.we3.ui.theme.errorDarkHighContrast
import org.project.we3.ui.theme.errorDarkMediumContrast
import org.project.we3.ui.theme.errorLight
import org.project.we3.ui.theme.errorLightHighContrast
import org.project.we3.ui.theme.errorLightMediumContrast
import org.project.we3.ui.theme.inverseOnSurfaceDark
import org.project.we3.ui.theme.inverseOnSurfaceDarkHighContrast
import org.project.we3.ui.theme.inverseOnSurfaceDarkMediumContrast
import org.project.we3.ui.theme.inverseOnSurfaceLight
import org.project.we3.ui.theme.inverseOnSurfaceLightHighContrast
import org.project.we3.ui.theme.inverseOnSurfaceLightMediumContrast
import org.project.we3.ui.theme.inversePrimaryDark
import org.project.we3.ui.theme.inversePrimaryDarkHighContrast
import org.project.we3.ui.theme.inversePrimaryDarkMediumContrast
import org.project.we3.ui.theme.inversePrimaryLight
import org.project.we3.ui.theme.inversePrimaryLightHighContrast
import org.project.we3.ui.theme.inversePrimaryLightMediumContrast
import org.project.we3.ui.theme.inverseSurfaceDark
import org.project.we3.ui.theme.inverseSurfaceDarkHighContrast
import org.project.we3.ui.theme.inverseSurfaceDarkMediumContrast
import org.project.we3.ui.theme.inverseSurfaceLight
import org.project.we3.ui.theme.inverseSurfaceLightHighContrast
import org.project.we3.ui.theme.inverseSurfaceLightMediumContrast
import org.project.we3.ui.theme.onBackgroundDark
import org.project.we3.ui.theme.onBackgroundDarkHighContrast
import org.project.we3.ui.theme.onBackgroundDarkMediumContrast
import org.project.we3.ui.theme.onBackgroundLight
import org.project.we3.ui.theme.onBackgroundLightHighContrast
import org.project.we3.ui.theme.onBackgroundLightMediumContrast
import org.project.we3.ui.theme.onErrorContainerDark
import org.project.we3.ui.theme.onErrorContainerDarkHighContrast
import org.project.we3.ui.theme.onErrorContainerDarkMediumContrast
import org.project.we3.ui.theme.onErrorContainerLight
import org.project.we3.ui.theme.onErrorContainerLightHighContrast
import org.project.we3.ui.theme.onErrorContainerLightMediumContrast
import org.project.we3.ui.theme.onErrorDark
import org.project.we3.ui.theme.onErrorDarkHighContrast
import org.project.we3.ui.theme.onErrorDarkMediumContrast
import org.project.we3.ui.theme.onErrorLight
import org.project.we3.ui.theme.onErrorLightHighContrast
import org.project.we3.ui.theme.onErrorLightMediumContrast
import org.project.we3.ui.theme.onPrimaryContainerDark
import org.project.we3.ui.theme.onPrimaryContainerDarkHighContrast
import org.project.we3.ui.theme.onPrimaryContainerDarkMediumContrast
import org.project.we3.ui.theme.onPrimaryContainerLight
import org.project.we3.ui.theme.onPrimaryContainerLightHighContrast
import org.project.we3.ui.theme.onPrimaryContainerLightMediumContrast
import org.project.we3.ui.theme.onPrimaryDark
import org.project.we3.ui.theme.onPrimaryDarkHighContrast
import org.project.we3.ui.theme.onPrimaryDarkMediumContrast
import org.project.we3.ui.theme.onPrimaryLight
import org.project.we3.ui.theme.onPrimaryLightHighContrast
import org.project.we3.ui.theme.onPrimaryLightMediumContrast
import org.project.we3.ui.theme.onSecondaryContainerDark
import org.project.we3.ui.theme.onSecondaryContainerDarkHighContrast
import org.project.we3.ui.theme.onSecondaryContainerDarkMediumContrast
import org.project.we3.ui.theme.onSecondaryContainerLight
import org.project.we3.ui.theme.onSecondaryContainerLightHighContrast
import org.project.we3.ui.theme.onSecondaryContainerLightMediumContrast
import org.project.we3.ui.theme.onSecondaryDark
import org.project.we3.ui.theme.onSecondaryDarkHighContrast
import org.project.we3.ui.theme.onSecondaryDarkMediumContrast
import org.project.we3.ui.theme.onSecondaryLight
import org.project.we3.ui.theme.onSecondaryLightHighContrast
import org.project.we3.ui.theme.onSecondaryLightMediumContrast
import org.project.we3.ui.theme.onSurfaceDark
import org.project.we3.ui.theme.onSurfaceDarkHighContrast
import org.project.we3.ui.theme.onSurfaceDarkMediumContrast
import org.project.we3.ui.theme.onSurfaceLight
import org.project.we3.ui.theme.onSurfaceLightHighContrast
import org.project.we3.ui.theme.onSurfaceLightMediumContrast
import org.project.we3.ui.theme.onSurfaceVariantDark
import org.project.we3.ui.theme.onSurfaceVariantDarkHighContrast
import org.project.we3.ui.theme.onSurfaceVariantDarkMediumContrast
import org.project.we3.ui.theme.onSurfaceVariantLight
import org.project.we3.ui.theme.onSurfaceVariantLightHighContrast
import org.project.we3.ui.theme.onSurfaceVariantLightMediumContrast
import org.project.we3.ui.theme.onTertiaryContainerDark
import org.project.we3.ui.theme.onTertiaryContainerDarkHighContrast
import org.project.we3.ui.theme.onTertiaryContainerDarkMediumContrast
import org.project.we3.ui.theme.onTertiaryContainerLight
import org.project.we3.ui.theme.onTertiaryContainerLightHighContrast
import org.project.we3.ui.theme.onTertiaryContainerLightMediumContrast
import org.project.we3.ui.theme.onTertiaryDark
import org.project.we3.ui.theme.onTertiaryDarkHighContrast
import org.project.we3.ui.theme.onTertiaryDarkMediumContrast
import org.project.we3.ui.theme.onTertiaryLight
import org.project.we3.ui.theme.onTertiaryLightHighContrast
import org.project.we3.ui.theme.onTertiaryLightMediumContrast
import org.project.we3.ui.theme.outlineDark
import org.project.we3.ui.theme.outlineDarkHighContrast
import org.project.we3.ui.theme.outlineDarkMediumContrast
import org.project.we3.ui.theme.outlineLight
import org.project.we3.ui.theme.outlineLightHighContrast
import org.project.we3.ui.theme.outlineLightMediumContrast
import org.project.we3.ui.theme.outlineVariantDark
import org.project.we3.ui.theme.outlineVariantDarkHighContrast
import org.project.we3.ui.theme.outlineVariantDarkMediumContrast
import org.project.we3.ui.theme.outlineVariantLight
import org.project.we3.ui.theme.outlineVariantLightHighContrast
import org.project.we3.ui.theme.outlineVariantLightMediumContrast
import org.project.we3.ui.theme.primaryContainerDark
import org.project.we3.ui.theme.primaryContainerDarkHighContrast
import org.project.we3.ui.theme.primaryContainerDarkMediumContrast
import org.project.we3.ui.theme.primaryContainerLight
import org.project.we3.ui.theme.primaryContainerLightHighContrast
import org.project.we3.ui.theme.primaryContainerLightMediumContrast
import org.project.we3.ui.theme.primaryDark
import org.project.we3.ui.theme.primaryDarkHighContrast
import org.project.we3.ui.theme.primaryDarkMediumContrast
import org.project.we3.ui.theme.primaryLight
import org.project.we3.ui.theme.primaryLightHighContrast
import org.project.we3.ui.theme.primaryLightMediumContrast
import org.project.we3.ui.theme.scrimDark
import org.project.we3.ui.theme.scrimDarkHighContrast
import org.project.we3.ui.theme.scrimDarkMediumContrast
import org.project.we3.ui.theme.scrimLight
import org.project.we3.ui.theme.scrimLightHighContrast
import org.project.we3.ui.theme.scrimLightMediumContrast
import org.project.we3.ui.theme.secondaryContainerDark
import org.project.we3.ui.theme.secondaryContainerDarkHighContrast
import org.project.we3.ui.theme.secondaryContainerDarkMediumContrast
import org.project.we3.ui.theme.secondaryContainerLight
import org.project.we3.ui.theme.secondaryContainerLightHighContrast
import org.project.we3.ui.theme.secondaryContainerLightMediumContrast
import org.project.we3.ui.theme.secondaryDark
import org.project.we3.ui.theme.secondaryDarkHighContrast
import org.project.we3.ui.theme.secondaryDarkMediumContrast
import org.project.we3.ui.theme.secondaryLight
import org.project.we3.ui.theme.secondaryLightHighContrast
import org.project.we3.ui.theme.secondaryLightMediumContrast
import org.project.we3.ui.theme.surfaceBrightDark
import org.project.we3.ui.theme.surfaceBrightDarkHighContrast
import org.project.we3.ui.theme.surfaceBrightDarkMediumContrast
import org.project.we3.ui.theme.surfaceBrightLight
import org.project.we3.ui.theme.surfaceBrightLightHighContrast
import org.project.we3.ui.theme.surfaceBrightLightMediumContrast
import org.project.we3.ui.theme.surfaceContainerDark
import org.project.we3.ui.theme.surfaceContainerDarkHighContrast
import org.project.we3.ui.theme.surfaceContainerDarkMediumContrast
import org.project.we3.ui.theme.surfaceContainerHighDark
import org.project.we3.ui.theme.surfaceContainerHighDarkHighContrast
import org.project.we3.ui.theme.surfaceContainerHighDarkMediumContrast
import org.project.we3.ui.theme.surfaceContainerHighLight
import org.project.we3.ui.theme.surfaceContainerHighLightHighContrast
import org.project.we3.ui.theme.surfaceContainerHighLightMediumContrast
import org.project.we3.ui.theme.surfaceContainerHighestDark
import org.project.we3.ui.theme.surfaceContainerHighestDarkHighContrast
import org.project.we3.ui.theme.surfaceContainerHighestDarkMediumContrast
import org.project.we3.ui.theme.surfaceContainerHighestLight
import org.project.we3.ui.theme.surfaceContainerHighestLightHighContrast
import org.project.we3.ui.theme.surfaceContainerHighestLightMediumContrast
import org.project.we3.ui.theme.surfaceContainerLight
import org.project.we3.ui.theme.surfaceContainerLightHighContrast
import org.project.we3.ui.theme.surfaceContainerLightMediumContrast
import org.project.we3.ui.theme.surfaceContainerLowDark
import org.project.we3.ui.theme.surfaceContainerLowDarkHighContrast
import org.project.we3.ui.theme.surfaceContainerLowDarkMediumContrast
import org.project.we3.ui.theme.surfaceContainerLowLight
import org.project.we3.ui.theme.surfaceContainerLowLightHighContrast
import org.project.we3.ui.theme.surfaceContainerLowLightMediumContrast
import org.project.we3.ui.theme.surfaceContainerLowestDark
import org.project.we3.ui.theme.surfaceContainerLowestDarkHighContrast
import org.project.we3.ui.theme.surfaceContainerLowestDarkMediumContrast
import org.project.we3.ui.theme.surfaceContainerLowestLight
import org.project.we3.ui.theme.surfaceContainerLowestLightHighContrast
import org.project.we3.ui.theme.surfaceContainerLowestLightMediumContrast
import org.project.we3.ui.theme.surfaceDark
import org.project.we3.ui.theme.surfaceDarkHighContrast
import org.project.we3.ui.theme.surfaceDarkMediumContrast
import org.project.we3.ui.theme.surfaceDimDark
import org.project.we3.ui.theme.surfaceDimDarkHighContrast
import org.project.we3.ui.theme.surfaceDimDarkMediumContrast
import org.project.we3.ui.theme.surfaceDimLight
import org.project.we3.ui.theme.surfaceDimLightHighContrast
import org.project.we3.ui.theme.surfaceDimLightMediumContrast
import org.project.we3.ui.theme.surfaceLight
import org.project.we3.ui.theme.surfaceLightHighContrast
import org.project.we3.ui.theme.surfaceLightMediumContrast
import org.project.we3.ui.theme.surfaceVariantDark
import org.project.we3.ui.theme.surfaceVariantDarkHighContrast
import org.project.we3.ui.theme.surfaceVariantDarkMediumContrast
import org.project.we3.ui.theme.surfaceVariantLight
import org.project.we3.ui.theme.surfaceVariantLightHighContrast
import org.project.we3.ui.theme.surfaceVariantLightMediumContrast
import org.project.we3.ui.theme.tertiaryContainerDark
import org.project.we3.ui.theme.tertiaryContainerDarkHighContrast
import org.project.we3.ui.theme.tertiaryContainerDarkMediumContrast
import org.project.we3.ui.theme.tertiaryContainerLight
import org.project.we3.ui.theme.tertiaryContainerLightHighContrast
import org.project.we3.ui.theme.tertiaryContainerLightMediumContrast
import org.project.we3.ui.theme.tertiaryDark
import org.project.we3.ui.theme.tertiaryDarkHighContrast
import org.project.we3.ui.theme.tertiaryDarkMediumContrast
import org.project.we3.ui.theme.tertiaryLight
import org.project.we3.ui.theme.tertiaryLightHighContrast
import org.project.we3.ui.theme.tertiaryLightMediumContrast

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      
      darkTheme -> darkScheme
      else -> lightScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}