package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CoachCharacterImage
import com.example.ui.components.CoachPose
import com.example.ui.components.GlowingGradientButton
import com.example.ui.components.NeonGlassCard
import com.example.ui.theme.FitBlack
import com.example.ui.theme.FitCardBackground
import com.example.ui.theme.FitCardBorder
import com.example.ui.theme.FitCardBorderNeon
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanDark
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleGlow
import com.example.ui.theme.TextDarkGray
import com.example.ui.theme.TextLightGray
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextWhite
import java.util.Locale

@Composable
fun MeditationScreen(
    selectedDurationMinutes: Int,
    secondsRemaining: Int,
    isMeditationRunning: Boolean,
    breathingPhase: String,
    onSelectDuration: (Int) -> Unit,
    onToggleMeditation: () -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath_scale"
    )

    val formattedRemaining = String.format(
        Locale.US,
        "%02d:%02d",
        secondsRemaining / 60,
        secondsRemaining % 60
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .testTag("meditation_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(FitCardBackground)
                    .border(1.dp, FitCardBorder, RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "MIND & MEDITATION",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Cortisol reduction & parasympathetic recovery",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Duration Selectors: 5 min, 10 min, 20 min, 30 min
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(5, 10, 20, 30).forEach { mins ->
                val isSelected = selectedDurationMinutes == mins
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) NeonCyan.copy(alpha = 0.22f) else Color(0xFF090D18)
                        )
                        .border(
                            1.2.dp,
                            if (isSelected) NeonCyan else FitCardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectDuration(mins) }
                        .testTag("meditation_duration_$mins"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$mins min",
                        color = if (isSelected) TextWhite else TextMutedGray,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Central Breathing Circle visualizer
        NeonGlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = NeonCyan.copy(alpha = 0.5f),
            cornerRadius = 24.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 3D Master Coach Shubham Zen Mindfulness Character
                CoachCharacterImage(
                    modifier = Modifier.size(100.dp, 130.dp),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = NeonCyan,
                    pose = CoachPose.MEDITATION,
                    showPoseBadge = true,
                    customBadgeText = "MINDFULNESS ZEN",
                    allowPicker = true,
                    showPickerIconBadge = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                Box(
                    modifier = Modifier.size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer pulsating neon halo
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .scale(if (isMeditationRunning) breathScale else 1f)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        NeonCyan.copy(alpha = 0.35f),
                                        NeonPurple.copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Inner circle
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF0C243B), Color(0xFF0B1726))
                                )
                            )
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isMeditationRunning) breathingPhase.uppercase() else "BREATHE",
                                color = NeonCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = formattedRemaining,
                                color = TextWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (isMeditationRunning) "Focus on your breath: Inhale 4s, Hold 4s, Exhale 4s" else "Calm your nervous system to accelerate athletic muscle recovery",
                    color = TextLightGray,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Play / Pause Toggle Button
                GlowingGradientButton(
                    text = if (isMeditationRunning) "Pause Session" else "Begin Meditation",
                    icon = if (isMeditationRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    gradient = Brush.horizontalGradient(listOf(Color(0xFF0891B2), Color(0xFF00F0FF))),
                    testTag = "toggle_meditation_button",
                    onClick = onToggleMeditation
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
