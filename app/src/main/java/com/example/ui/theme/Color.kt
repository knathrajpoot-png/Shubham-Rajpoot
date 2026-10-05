package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core Brand Colors for THEFIT SHUBHAM
val FitBlack = Color(0xFF05070B)
val FitDarkBackground = Color(0xFF070A10)
val FitSurface = Color(0xFF0D121F)
val FitSurfaceElevated = Color(0xFF141B2D)
val FitCardBackground = Color(0xFF0F1626)
val FitCardBorder = Color(0xFF261D3B)
val FitCardBorderNeon = Color(0xFF9D4EDD)

// Neon Accents
val NeonPurple = Color(0xFFA855F7)
val NeonPurpleGlow = Color(0xFFC084FC)
val NeonPurpleDark = Color(0xFF7E22CE)
val NeonPurpleDeep = Color(0xFF581C87)

val NeonCyan = Color(0xFF00F0FF)
val NeonCyanDark = Color(0xFF0891B2)
val NeonCyanLight = Color(0xFF67E8F9)

val NeonGreen = Color(0xFF10B981)
val NeonGreenGlow = Color(0xFF34D399)
val NeonGreenAccent = Color(0xFF00E676)

val NeonOrange = Color(0xFFFF6D00)
val NeonOrangeGlow = Color(0xFFFB923C)
val NeonOrangeDark = Color(0xFFC2410C)

val NeonRed = Color(0xFFEF4444)

// Text Colors
val TextWhite = Color(0xFFFFFFFF)
val TextLightGray = Color(0xFFE2E8F0)
val TextMutedGray = Color(0xFF94A3B8)
val TextDarkGray = Color(0xFF64748B)

// Gradients
val PurpleNeonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF9333EA), Color(0xFFA855F7), Color(0xFFC084FC))
)

val CyanNeonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF0891B2), Color(0xFF00F0FF))
)

val OrangeNeonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFEA580C), Color(0xFFFF6D00))
)

val GreenNeonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF059669), Color(0xFF10B981))
)

val CardGlowGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF181B2C), Color(0xFF0C101B))
)

val DarkHeaderGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF19102E), Color(0xFF070A10))
)
