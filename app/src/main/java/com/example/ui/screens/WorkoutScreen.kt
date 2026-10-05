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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exercise
import com.example.data.model.WorkoutCategoryItem
import com.example.ui.components.CoachCharacterImage
import com.example.ui.components.GlowingGradientButton
import com.example.ui.components.NeonGlassCard
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

fun getCategoryIcon(categoryName: String): ImageVector {
    val nameLower = categoryName.lowercase()
    return when {
        nameLower.contains("run") || nameLower.contains("calf") || nameLower.contains("legs") -> Icons.Default.DirectionsRun
        nameLower.contains("stretch") || nameLower.contains("mobility") || nameLower.contains("recovery") || nameLower.contains("balance") -> Icons.Default.SelfImprovement
        nameLower.contains("core") || nameLower.contains("plyo") || nameLower.contains("functional") || nameLower.contains("skill") -> Icons.Default.Bolt
        nameLower.contains("full body") -> Icons.Default.LocalFireDepartment
        else -> Icons.Default.FitnessCenter
    }
}

@Composable
fun WorkoutScreen(
    categories: List<WorkoutCategoryItem>,
    initialSelectedCategory: WorkoutCategoryItem? = null,
    onSelectCategory: (WorkoutCategoryItem) -> Unit = {},
    onStartWorkout: (WorkoutCategoryItem) -> Unit
) {
    var selectedCategoryForDetail by remember { mutableStateOf<WorkoutCategoryItem?>(initialSelectedCategory) }

    if (selectedCategoryForDetail != null) {
        WorkoutDetailView(
            category = selectedCategoryForDetail!!,
            onBack = { selectedCategoryForDetail = null },
            onStartSession = {
                val cat = selectedCategoryForDetail!!
                selectedCategoryForDetail = null
                onStartWorkout(cat)
            }
        )
    } else {
        WorkoutCategoriesListView(
            categories = categories,
            onCategoryClick = { 
                selectedCategoryForDetail = it
                onSelectCategory(it)
            }
        )
    }
}

private enum class WorkoutFilterTab {
    ALL,
    SPECIALTY_DISCIPLINES,
    CORE_SPLITS
}

@Composable
private fun WorkoutCategoriesListView(
    categories: List<WorkoutCategoryItem>,
    onCategoryClick: (WorkoutCategoryItem) -> Unit
) {
    var selectedTab by remember { mutableStateOf(WorkoutFilterTab.ALL) }

    val filteredCategories = when (selectedTab) {
        WorkoutFilterTab.ALL -> categories
        WorkoutFilterTab.SPECIALTY_DISCIPLINES -> categories.filter { it.isSpecialtyCategory }
        WorkoutFilterTab.CORE_SPLITS -> categories.filter { !it.isSpecialtyCategory }
    }

    val specialtyCount = categories.count { it.isSpecialtyCategory }
    val coreCount = categories.count { !it.isSpecialtyCategory }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .padding(horizontal = 16.dp)
            .testTag("workout_list_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Main Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "WORKOUT CATEGORIES",
                        color = TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${categories.size} AI routines • 115 exercises",
                        color = TextMutedGray,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonPurple.copy(alpha = 0.2f))
                        .border(1.dp, NeonPurpleGlow.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${categories.size} TOTAL",
                        color = NeonPurpleGlow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Tabs (All / New Disciplines / Core Splits)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryFilterChip(
                    text = "All (${categories.size})",
                    isSelected = selectedTab == WorkoutFilterTab.ALL,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = WorkoutFilterTab.ALL }
                )
                CategoryFilterChip(
                    text = "Specialty ($specialtyCount)",
                    badge = "NEW",
                    isSelected = selectedTab == WorkoutFilterTab.SPECIALTY_DISCIPLINES,
                    modifier = Modifier.weight(1.3f),
                    onClick = { selectedTab = WorkoutFilterTab.SPECIALTY_DISCIPLINES }
                )
                CategoryFilterChip(
                    text = "Core Splits ($coreCount)",
                    isSelected = selectedTab == WorkoutFilterTab.CORE_SPLITS,
                    modifier = Modifier.weight(1.2f),
                    onClick = { selectedTab = WorkoutFilterTab.CORE_SPLITS }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Hero Spotlight Banner for Specialty Disciplines
        if (selectedTab != WorkoutFilterTab.CORE_SPLITS) {
            item {
                NeonGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonPurple.copy(alpha = 0.65f),
                    cornerRadius = 22.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1.3f)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF7E22CE), Color(0xFFA855F7), Color(0xFF00F0FF))
                                        )
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = TextWhite,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "SPECIALTY DISCIPLINES",
                                        color = TextWhite,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.6.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "15 Advanced Training Disciplines",
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Mobility, Kettlebell, Resistance Band, Glutes, Calves, Forearms, Neck, Plyo, Balance & Recovery.",
                                color = TextLightGray,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        CoachCharacterImage(
                            modifier = Modifier.size(72.dp, 96.dp),
                            shape = RoundedCornerShape(18.dp),
                            borderColor = NeonPurpleGlow,
                            allowPicker = true,
                            showPickerIconBadge = true
                        )
                    }
                }
            }
        }

        // Category Cards List
        items(filteredCategories) { cat ->
            WorkoutCategoryCard(
                category = cat,
                onClick = { onCategoryClick(cat) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun CategoryFilterChip(
    text: String,
    badge: String? = null,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) {
                    Brush.horizontalGradient(
                        listOf(Color(0xFF7E22CE), Color(0xFFA855F7))
                    )
                } else {
                    Brush.horizontalGradient(
                        listOf(Color(0xFF0C101B), Color(0xFF111726))
                    )
                }
            )
            .border(
                width = 1.dp,
                color = if (isSelected) NeonPurpleGlow else FitCardBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = if (isSelected) TextWhite else TextLightGray,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
            if (badge != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) Color(0xFF00F0FF) else NeonPurple)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badge,
                        color = if (isSelected) FitBlack else TextWhite,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkoutCategoryCard(
    category: WorkoutCategoryItem,
    onClick: () -> Unit
) {
    val icon = getCategoryIcon(category.name)

    NeonGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("workout_cat_${category.name.lowercase().replace(" ", "_")}"),
        borderColor = if (category.isSpecialtyCategory) NeonPurple.copy(alpha = 0.55f) else FitCardBorderNeon.copy(alpha = 0.4f),
        cornerRadius = 20.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1.3f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = category.name.uppercase(),
                        color = TextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp
                    )
                    if (category.isSpecialtyCategory) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NeonPurple.copy(alpha = 0.25f))
                                .border(1.dp, NeonPurpleGlow.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "SPECIALTY",
                                color = NeonPurpleGlow,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = category.description,
                    color = TextMutedGray,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonPurple.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${category.exerciseCount} Exercises",
                            color = NeonPurpleGlow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonCyan.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${category.durationMinutes} min",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 3D Master Coach Shubham Category Character Pose
            CoachCharacterImage(
                modifier = Modifier.size(62.dp, 78.dp),
                shape = RoundedCornerShape(16.dp),
                borderColor = if (category.isSpecialtyCategory) NeonCyan.copy(alpha = 0.7f) else NeonPurpleGlow.copy(alpha = 0.7f),
                pose = getExercisePose("", category.name),
                showPoseBadge = true,
                allowPicker = false
            )
        }
    }
}

@Composable
fun WorkoutDetailView(
    category: WorkoutCategoryItem,
    onBack: () -> Unit,
    onStartSession: () -> Unit
) {
    val icon = getCategoryIcon(category.name)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .padding(horizontal = 16.dp)
            .testTag("workout_detail_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Header with Back button
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
                        text = "${category.name} Routine",
                        color = TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${category.exerciseCount} Exercises • ${category.durationMinutes} min",
                        color = NeonCyan,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Hero Banner with Start Button
        item {
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
                        Text(
                            text = category.description,
                            color = TextLightGray,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        GlowingGradientButton(
                            text = "Start Workout",
                            icon = Icons.Default.PlayArrow,
                            testTag = "start_workout_session_button",
                            onClick = onStartSession
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    CoachCharacterImage(
                        modifier = Modifier.size(80.dp, 105.dp),
                        shape = RoundedCornerShape(20.dp),
                        borderColor = NeonPurpleGlow,
                        pose = getExercisePose("", category.name),
                        showPoseBadge = true,
                        allowPicker = true,
                        showPickerIconBadge = true
                    )
                }
            }
        }

        item {
            Text(
                text = "EXERCISE LIST (${category.exercises.size})",
                color = TextLightGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        items(category.exercises) { ex ->
            ExerciseCardItem(exercise = ex)
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun ExerciseCardItem(exercise: Exercise) {
    val icon = when {
        exercise.category.lowercase().contains("chest") || exercise.targetMuscles.lowercase().contains("chest") -> Icons.Default.FitnessCenter
        exercise.category.lowercase().contains("leg") || exercise.targetMuscles.lowercase().contains("quad") -> Icons.Default.DirectionsRun
        exercise.category.lowercase().contains("core") -> Icons.Default.Bolt
        exercise.category.lowercase().contains("stretch") -> Icons.Default.SelfImprovement
        else -> Icons.Default.FitnessCenter
    }

    NeonGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("exercise_card_${exercise.id}"),
        borderColor = FitCardBorderNeon.copy(alpha = 0.35f),
        cornerRadius = 18.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 3D Master Coach Shubham Exercise Demo Character
                CoachCharacterImage(
                    modifier = Modifier.size(54.dp, 68.dp),
                    shape = RoundedCornerShape(14.dp),
                    borderColor = NeonPurpleGlow.copy(alpha = 0.6f),
                    pose = getExercisePose(exercise.name, exercise.category),
                    showPoseBadge = true,
                    allowPicker = false
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        color = TextWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Target: ${exercise.targetMuscles}",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Difficulty badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (exercise.difficulty) {
                                "Advanced" -> NeonOrange.copy(alpha = 0.2f)
                                "Intermediate" -> NeonPurple.copy(alpha = 0.2f)
                                else -> NeonGreen.copy(alpha = 0.2f)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = exercise.difficulty,
                        color = when (exercise.difficulty) {
                            "Advanced" -> NeonOrange
                            "Intermediate" -> NeonPurpleGlow
                            else -> NeonGreen
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Instructions
            Text(
                text = exercise.instructions,
                color = TextLightGray,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Safety Tips with security shield badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NeonGreen.copy(alpha = 0.08f))
                    .border(1.dp, NeonGreen.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Safety",
                    tint = NeonGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Safety Tip: ${exercise.safetyTips}",
                    color = NeonGreen,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Metrics Row: Sets, Reps, Rest
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricChip(label = "Sets", value = "${exercise.sets}", modifier = Modifier.weight(1f))
                MetricChip(label = "Reps", value = exercise.reps, modifier = Modifier.weight(1f))
                MetricChip(label = "Rest", value = "${exercise.restSeconds}s", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MetricChip(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF090D18))
            .border(1.dp, FitCardBorder, RoundedCornerShape(8.dp))
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = label, color = TextDarkGray, fontSize = 9.sp)
        }
    }
}
