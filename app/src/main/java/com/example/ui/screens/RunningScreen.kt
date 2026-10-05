package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RunSession
import com.example.ui.components.CircularNeonProgress
import com.example.ui.components.CoachCharacterImage
import com.example.ui.components.CoachPose
import com.example.ui.components.GlowingGradientButton
import com.example.ui.components.NeonGlassCard
import com.example.ui.theme.FitBlack
import com.example.ui.theme.FitCardBackground
import com.example.ui.theme.FitCardBorder
import com.example.ui.theme.FitCardBorderNeon
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeDark
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextDarkGray
import com.example.ui.theme.TextLightGray
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RunningScreen(
    isRunningActive: Boolean,
    runDurationSeconds: Long,
    runDistanceKm: Float,
    runCalories: Int,
    runPace: String,
    pastRuns: List<RunSession>,
    onToggleRun: () -> Unit,
    onFinishRun: () -> Unit,
    onBack: () -> Unit
) {
    val durationFormatted = String.format(
        Locale.US,
        "%02d:%02d",
        runDurationSeconds / 60,
        runDurationSeconds % 60
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("running_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
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
                        text = "RUNNING DASHBOARD",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "GPS & Pedometer Live Speedometer",
                        color = NeonOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Live Speedometer / Distance Gauge
        item {
            NeonGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonOrange.copy(alpha = 0.6f),
                cornerRadius = 24.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // 3D Master Coach Shubham Running Sprint Character
                        CoachCharacterImage(
                            modifier = Modifier.size(95.dp, 125.dp),
                            shape = RoundedCornerShape(20.dp),
                            borderColor = NeonOrange,
                            pose = CoachPose.RUNNING,
                            showPoseBadge = true,
                            customBadgeText = if (isRunningActive) "SPRINTING" else "RUNNER POSE",
                            allowPicker = true,
                            showPickerIconBadge = true
                        )

                        // Circular Speedometer / Distance Gauge
                        CircularNeonProgress(
                            progressPercent = ((runDistanceKm / 5.0f) * 100).toInt().coerceIn(0, 100),
                            size = 140.dp,
                            strokeWidth = 10.dp,
                            accentColor = NeonOrange
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = String.format(Locale.US, "%.2f", runDistanceKm),
                                    color = TextWhite,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = (-0.5).sp
                                )
                                Text(
                                    text = "KM",
                                    color = NeonOrange,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isRunningActive) "LIVE" else "READY",
                                    color = if (isRunningActive) NeonOrangeGlow else TextDarkGray,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 3 Real-time metrics: Duration, Pace, Calories
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        LiveRunStat(label = "TIME", value = durationFormatted)
                        LiveRunStat(label = "PACE", value = runPace)
                        LiveRunStat(label = "CALORIES", value = "$runCalories kcal")
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    // Action Controls: Start/Pause and Finish
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Start / Pause
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFEA580C), Color(0xFFFF6D00))
                                    )
                                )
                                .clickable { onToggleRun() }
                                .padding(vertical = 14.dp)
                                .testTag("toggle_run_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isRunningActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = TextWhite,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isRunningActive) "Pause Run" else if (runDurationSeconds > 0) "Resume Run" else "Start Run",
                                    color = TextWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Stop & Save Run (if started)
                        if (runDurationSeconds > 0) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF1E1724))
                                    .border(1.2.dp, NeonOrange, RoundedCornerShape(16.dp))
                                    .clickable { onFinishRun() }
                                    .padding(horizontal = 18.dp, vertical = 14.dp)
                                    .testTag("finish_run_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = "Stop Run",
                                    tint = NeonOrange,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // PREVIOUS RUNNING SESSIONS
        item {
            Text(
                text = "PREVIOUS RUNS & SPLITS",
                color = TextLightGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        if (pastRuns.isEmpty()) {
            item {
                NeonGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonOrange.copy(alpha = 0.3f),
                    cornerRadius = 18.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsRun,
                            contentDescription = null,
                            tint = NeonOrange,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "No runs recorded yet",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap 'Start Run' above to begin your GPS & cadence tracker",
                            color = TextMutedGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(pastRuns) { run ->
                NeonGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonOrange.copy(alpha = 0.35f),
                    cornerRadius = 16.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(NeonOrange.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsRun,
                                    contentDescription = null,
                                    tint = NeonOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${String.format(Locale.US, "%.2f", run.distanceKm)} km Run",
                                    color = TextWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(run.timestamp)),
                                    color = TextMutedGray,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${run.caloriesBurned} kcal",
                                color = NeonOrange,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${run.durationSeconds / 60}m ${run.durationSeconds % 60}s",
                                color = TextDarkGray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun LiveRunStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = TextDarkGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
    }
}
