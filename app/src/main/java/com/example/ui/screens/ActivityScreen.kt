package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.model.CompletedWorkout
import com.example.data.model.DailyActivity
import com.example.data.model.UserProfile
import com.example.ui.components.CircularNeonProgress
import com.example.ui.components.NeonGlassCard
import com.example.ui.theme.FitBlack
import com.example.ui.theme.FitCardBackground
import com.example.ui.theme.FitCardBorder
import com.example.ui.theme.FitCardBorderNeon
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleDark
import com.example.ui.theme.NeonPurpleGlow
import com.example.ui.theme.TextDarkGray
import com.example.ui.theme.TextLightGray
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextWhite
import java.util.Locale

@Composable
fun ActivityScreen(
    userProfile: UserProfile?,
    dailyActivity: DailyActivity?,
    recentWorkouts: List<CompletedWorkout>
) {
    val steps = dailyActivity?.steps ?: 6420
    val stepGoal = dailyActivity?.stepGoal ?: 10000
    val calories = dailyActivity?.caloriesBurned ?: 520
    val calorieGoal = dailyActivity?.calorieGoal ?: 650
    val water = dailyActivity?.waterIntakeLiters ?: 2.4f
    val waterGoal = userProfile?.waterTargetLiters ?: 3.5f
    val exerciseMin = dailyActivity?.exerciseTimeMin ?: 45
    val exerciseGoal = dailyActivity?.exerciseGoalMin ?: 60

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .padding(horizontal = 16.dp)
            .testTag("activity_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = "BIOMETRIC & ACTIVITY RADAR",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Real-time telemetry, calorie expenditures & workout logs",
                    color = TextMutedGray,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Circular Neon Rings Composite
        item {
            NeonGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonPurple.copy(alpha = 0.5f),
                cornerRadius = 24.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Circular Progress Dial
                    CircularNeonProgress(
                        progressPercent = ((steps / stepGoal.toFloat()) * 100).toInt().coerceIn(0, 100),
                        size = 115.dp,
                        strokeWidth = 9.dp,
                        accentColor = NeonCyan
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$steps",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "/ $stepGoal",
                                color = NeonCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "steps",
                                color = TextDarkGray,
                                fontSize = 9.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Progress breakdown indicators
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ActivityMetricBar(
                            title = "Calories",
                            current = "$calories",
                            target = "$calorieGoal kcal",
                            progress = (calories / calorieGoal.toFloat()).coerceIn(0f, 1f),
                            color = NeonOrange
                        )
                        ActivityMetricBar(
                            title = "Active Workout",
                            current = "$exerciseMin",
                            target = "$exerciseGoal min",
                            progress = (exerciseMin / exerciseGoal.toFloat()).coerceIn(0f, 1f),
                            color = NeonPurple
                        )
                        ActivityMetricBar(
                            title = "Water Intake",
                            current = "${String.format(Locale.US, "%.1f", water)}L",
                            target = "${String.format(Locale.US, "%.1f", waterGoal)}L",
                            progress = (water / waterGoal).coerceIn(0f, 1f),
                            color = NeonCyan
                        )
                    }
                }
            }
        }

        // WEEKLY PROGRESS BAR CHART
        item {
            Text(
                text = "WEEKLY ACTIVITY PROGRESS",
                color = TextLightGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        item {
            NeonGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = FitCardBorderNeon.copy(alpha = 0.4f),
                cornerRadius = 20.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daily Calorie Burn Trend",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Avg 580 kcal",
                            color = NeonOrange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Weekly Bars (Mon - Sun)
                    val days = listOf("Mon" to 0.75f, "Tue" to 0.90f, "Wed" to 0.65f, "Thu" to 0.85f, "Fri" to 0.95f, "Sat" to 0.70f, "Sun" to 0.80f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEach { (day, fraction) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(18.dp)
                                        .height((90 * fraction).dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(NeonPurpleGlow, NeonPurpleDark)
                                            )
                                        )
                                        .border(1.dp, NeonPurpleGlow.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = day,
                                    color = if (day == "Sun") NeonCyan else TextMutedGray,
                                    fontSize = 10.sp,
                                    fontWeight = if (day == "Sun") FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }

        // MONTHLY GOAL SUMMARY & RECENT LOGS
        item {
            Text(
                text = "RECENT WORKOUT SESSIONS",
                color = TextLightGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        if (recentWorkouts.isEmpty()) {
            item {
                NeonGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 16.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = NeonPurple,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No workouts logged yet",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Complete your first workout to see activity telemetry here",
                            color = TextMutedGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(recentWorkouts) { log ->
                NeonGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = FitCardBorderNeon.copy(alpha = 0.35f),
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(NeonPurple.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = NeonPurpleGlow,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = log.title,
                                    color = TextWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${log.durationMinutes} min • ${log.category}",
                                    color = TextMutedGray,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeonOrange.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "-${log.caloriesBurned} kcal",
                                color = NeonOrange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
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
private fun ActivityMetricBar(
    title: String,
    current: String,
    target: String,
    progress: Float,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = TextLightGray, fontSize = 11.sp)
            Text(text = "$current / $target", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFF141926)
        )
    }
}
