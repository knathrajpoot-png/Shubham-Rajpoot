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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyActivity
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutCategoryItem
import com.example.ui.components.CircularNeonProgress
import com.example.ui.components.CoachCharacterImage
import com.example.ui.components.GlowingGradientButton
import com.example.ui.components.NeonGlassCard
import com.example.ui.components.QuickActionButton
import com.example.ui.components.StatCard
import com.example.ui.components.getExercisePose
import com.example.ui.theme.FitBlack
import com.example.ui.theme.FitCardBackground
import com.example.ui.theme.FitCardBorder
import com.example.ui.theme.FitCardBorderNeon
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleGlow
import com.example.ui.theme.TextDarkGray
import com.example.ui.theme.TextLightGray
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextWhite
import java.util.Locale

@Composable
fun HomeScreen(
    userProfile: UserProfile?,
    dailyActivity: DailyActivity?,
    workoutCategories: List<WorkoutCategoryItem> = emptyList(),
    onCategoryClick: (WorkoutCategoryItem) -> Unit = {},
    onQuickActionClick: (String) -> Unit, // "WORKOUT", "DIET", "RUNNING", "MEDITATION"
    onStartWorkoutClick: () -> Unit,
    onViewMealPlanClick: () -> Unit,
    onAddQuickWater: () -> Unit,
    onAddQuickSteps: () -> Unit
) {
    val scrollState = rememberScrollState()
    val userName = userProfile?.name?.split(" ")?.firstOrNull() ?: "Shubham"
    val calories = dailyActivity?.caloriesBurned ?: 520
    val steps = dailyActivity?.steps ?: 6420
    val water = dailyActivity?.waterIntakeLiters ?: 2.4f
    val streak = dailyActivity?.streakDays ?: 7
    val goal = userProfile?.goal ?: "Muscle Gain"
    val dailyCalTarget = userProfile?.dailyCalorieTarget ?: 2500
    val proteinTarget = userProfile?.proteinTargetGrams ?: 160

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .testTag("home_screen")
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Greeting Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Good Morning, $userName! 👋",
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.3).sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Ready to crush your goals today?",
                    color = TextMutedGray,
                    fontSize = 13.sp
                )
            }

            // Streak Pill Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFEA580C).copy(alpha = 0.25f), Color(0xFFFF6D00).copy(alpha = 0.25f))
                        )
                    )
                    .border(1.dp, NeonOrange.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Whatshot,
                        contentDescription = "Streak",
                        tint = NeonOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$streak Days Streak",
                        color = NeonOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4 Key Dashboard Stats Grid: Calories, Steps, Water, Streak
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Burned",
                value = "$calories",
                unit = "kcal",
                icon = Icons.Default.LocalFireDepartment,
                accentColor = NeonOrange
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Today",
                value = "$steps",
                unit = "steps",
                icon = Icons.Default.DirectionsRun,
                accentColor = NeonCyan,
                onTap = onAddQuickSteps
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Hydration",
                value = String.format(Locale.US, "%.1f", water),
                unit = "L",
                icon = Icons.Default.WaterDrop,
                accentColor = NeonCyan,
                onTap = onAddQuickWater
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Consistency",
                value = "$streak",
                unit = "days",
                icon = Icons.Default.Whatshot,
                accentColor = NeonPurpleGlow
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // QUICK ACTIONS (4 Large Colorful Buttons: WORKOUT, DIET PLAN, RUNNING, MEDITATION)
        Text(
            text = "QUICK ACTIONS",
            color = TextLightGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // WORKOUT (Purple)
            QuickActionButton(
                title = "WORKOUT",
                icon = Icons.Default.FitnessCenter,
                color = NeonPurple,
                modifier = Modifier.weight(1f),
                onClick = { onQuickActionClick("WORKOUT") }
            )

            // DIET PLAN (Green)
            QuickActionButton(
                title = "DIET PLAN",
                icon = Icons.Default.Restaurant,
                color = NeonGreen,
                modifier = Modifier.weight(1f),
                onClick = { onQuickActionClick("DIET") }
            )

            // RUNNING (Orange)
            QuickActionButton(
                title = "RUNNING",
                icon = Icons.Default.DirectionsRun,
                color = NeonOrange,
                modifier = Modifier.weight(1f),
                onClick = { onQuickActionClick("RUNNING") }
            )

            // MEDITATION (Cyan)
            QuickActionButton(
                title = "MEDITATION",
                icon = Icons.Default.SelfImprovement,
                color = NeonCyan,
                modifier = Modifier.weight(1f),
                onClick = { onQuickActionClick("MEDITATION") }
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // TODAY'S WORKOUT HERO CARD
        Text(
            text = "TODAY'S WORKOUT",
            color = TextLightGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        NeonGlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = NeonPurple.copy(alpha = 0.6f),
            cornerRadius = 24.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1.2f)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonPurple.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "RECOMMENDED BY AI COACH",
                            color = NeonPurpleGlow,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Full Body Home Workout",
                        color = TextWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "6 Exercises • 45 min",
                            color = TextLightGray,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Start Workout Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF7E22CE), Color(0xFFA855F7), Color(0xFF00F0FF))
                                )
                            )
                            .clickable { onStartWorkoutClick() }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .testTag("start_workout_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Start Workout",
                                color = TextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                // Master Coach Shubham Character Image (Same Face, Body, Style)
                CoachCharacterImage(
                    modifier = Modifier.size(90.dp, 120.dp),
                    shape = RoundedCornerShape(20.dp),
                    allowPicker = true,
                    showPickerIconBadge = true
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // WORKOUT CATEGORIES SECTION (NEW 3D DISCIPLINES CAROUSEL)
        if (workoutCategories.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "WORKOUT CATEGORIES",
                        color = TextLightGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "15 New 3D Disciplines • All Muscles",
                        color = NeonPurpleGlow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonPurple.copy(alpha = 0.2f))
                        .border(1.dp, NeonPurpleGlow.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .clickable { onQuickActionClick("WORKOUT") }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "View All (${workoutCategories.size})",
                            color = NeonPurpleGlow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = NeonPurpleGlow,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Carousel of 3D animated category cards
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(workoutCategories) { cat ->
                    HomeCategoryCard(
                        category = cat,
                        onClick = { onCategoryClick(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))
        }

        // TODAY'S MEAL PLAN CARD
        NeonGlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = NeonGreen.copy(alpha = 0.5f),
            cornerRadius = 22.dp,
            onClick = onViewMealPlanClick
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeonGreen.copy(alpha = 0.15f))
                                .border(1.dp, NeonGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Today's Meal Plan",
                                color = TextWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$dailyCalTarget kcal • High Protein",
                                color = NeonGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View Meal Plan",
                        tint = NeonGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Macronutrient Summary Bars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MacroPill(name = "Protein", value = "${proteinTarget}g", color = NeonPurple, modifier = Modifier.weight(1f))
                    MacroPill(name = "Carbs", value = "${userProfile?.carbsTargetGrams ?: 280}g", color = NeonCyan, modifier = Modifier.weight(1f))
                    MacroPill(name = "Fats", value = "${userProfile?.fatsTargetGrams ?: 65}g", color = NeonOrange, modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // GOAL PROGRESS & AI COACH SUGGESTION DUAL SECTION
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // GOAL PROGRESS CARD
            NeonGlassCard(
                modifier = Modifier.weight(1f),
                borderColor = NeonPurple.copy(alpha = 0.4f),
                cornerRadius = 20.dp
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Goal Progress",
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = goal,
                        color = NeonPurpleGlow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Circular Progress Neon Indicator
                    CircularNeonProgress(
                        progressPercent = 75,
                        size = 85.dp,
                        strokeWidth = 7.dp,
                        accentColor = NeonPurple
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "75%",
                                color = TextWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Completed",
                                color = TextMutedGray,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }

            // AI COACH SUGGESTION CARD
            NeonGlassCard(
                modifier = Modifier.weight(1.3f),
                borderColor = NeonCyan.copy(alpha = 0.4f),
                cornerRadius = 20.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI Coach Suggestion",
                            color = NeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\"Focus on protein intake post-workout and maintain progressive overload!\"",
                        color = TextLightGray,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF0C1929))
                            .border(1.dp, NeonCyan.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Personalized for ${userProfile?.weightKg?.toInt() ?: 75}kg • $goal",
                            color = TextMutedGray,
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun HomeCategoryCard(
    category: WorkoutCategoryItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D111A), Color(0xFF080B12))
                )
            )
            .border(
                1.dp,
                if (category.isSpecialtyCategory) NeonPurple.copy(alpha = 0.5f) else FitCardBorderNeon.copy(alpha = 0.35f),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 3D Master Coach Shubham Category Pose
            CoachCharacterImage(
                modifier = Modifier.size(64.dp, 80.dp),
                shape = RoundedCornerShape(16.dp),
                borderColor = if (category.isSpecialtyCategory) NeonCyan.copy(alpha = 0.7f) else NeonPurpleGlow.copy(alpha = 0.7f),
                pose = getExercisePose("", category.name),
                showPoseBadge = true,
                allowPicker = false
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = category.name,
                color = TextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "${category.exerciseCount} Ex",
                    color = NeonPurpleGlow,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "•",
                    color = TextDarkGray,
                    fontSize = 10.sp
                )
                Text(
                    text = "${category.durationMinutes}m",
                    color = NeonCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun MacroPill(
    name: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                color = TextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = name,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
