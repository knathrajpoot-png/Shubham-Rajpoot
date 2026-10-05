package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = NeonPurple,
    onPrimary = TextWhite,
    primaryContainer = NeonPurpleDark,
    onPrimaryContainer = TextWhite,
    secondary = NeonCyan,
    onSecondary = FitBlack,
    secondaryContainer = NeonCyanDark,
    onSecondaryContainer = TextWhite,
    tertiary = NeonOrange,
    onTertiary = TextWhite,
    background = FitBlack,
    onBackground = TextWhite,
    surface = FitSurface,
    onSurface = TextWhite,
    surfaceVariant = FitSurfaceElevated,
    onSurfaceVariant = TextMutedGray,
    outline = FitCardBorder,
    outlineVariant = FitCardBorderNeon,
    error = NeonRed,
    onError = TextWhite
)

val FitShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun TheFitShubhamTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        shapes = FitShapes,
        content = content
    )
}
