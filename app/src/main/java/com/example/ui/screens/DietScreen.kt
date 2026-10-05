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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Restaurant
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
import com.example.data.model.MealItem
import com.example.data.model.UserProfile
import com.example.ui.components.CoachCharacterImage
import com.example.ui.components.CoachPose
import com.example.ui.components.NeonGlassCard
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
fun DietScreen(
    userProfile: UserProfile?,
    meals: List<MealItem>,
    onToggleMeal: (String) -> Unit,
    onAddWater: () -> Unit
) {
    val totalCal = userProfile?.dailyCalorieTarget ?: 2500
    val protein = userProfile?.proteinTargetGrams ?: 160
    val carbs = userProfile?.carbsTargetGrams ?: 280
    val fats = userProfile?.fatsTargetGrams ?: 65
    val water = userProfile?.waterTargetLiters ?: 3.5f
    val goal = userProfile?.goal ?: "Muscle Gain"

    val completedMealsCount = meals.count { it.isCompleted }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .padding(horizontal = 16.dp)
            .testTag("diet_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "PERSONALIZED DIET PLAN",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Crafted for $goal • Indian Fitness Options",
                        color = NeonGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonGreen.copy(alpha = 0.15f))
                        .border(1.dp, NeonGreen.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$completedMealsCount/${meals.size} Logged",
                        color = NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Daily Macro Targets Card
        item {
            NeonGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen.copy(alpha = 0.5f),
                cornerRadius = 22.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonGreen.copy(alpha = 0.2f))
                                    .border(1.dp, NeonGreen, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "HIGH PROTEIN PLAN",
                                    color = NeonGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "$totalCal kcal",
                                color = TextWhite,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Daily Calorie Target for $goal",
                                color = TextMutedGray,
                                fontSize = 12.sp
                            )
                        }

                        // 3D Master Coach Shubham Nutrition & Physique Guide
                        CoachCharacterImage(
                            modifier = Modifier.size(80.dp, 105.dp),
                            shape = RoundedCornerShape(20.dp),
                            borderColor = NeonGreen,
                            pose = CoachPose.IDLE,
                            showPoseBadge = true,
                            customBadgeText = "COACH SHUBHAM",
                            allowPicker = true,
                            showPickerIconBadge = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Macro distribution row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DietMacroItem(title = "Protein", value = "${protein}g", color = NeonPurple, modifier = Modifier.weight(1f))
                        DietMacroItem(title = "Carbs", value = "${carbs}g", color = NeonCyan, modifier = Modifier.weight(1f))
                        DietMacroItem(title = "Fats", value = "${fats}g", color = NeonOrange, modifier = Modifier.weight(1f))
                        DietMacroItem(title = "Water", value = "${String.format(Locale.US, "%.1f", water)}L", color = NeonCyan, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Water Hydration Tracker Banner
        item {
            NeonGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan.copy(alpha = 0.4f),
                cornerRadius = 18.dp,
                onClick = onAddWater
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
                                .background(NeonCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Daily Hydration Target: ${String.format(Locale.US, "%.1f", water)} Liters",
                                color = TextWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tap to quickly log 250ml water glass",
                                color = NeonCyan,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonCyan.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "+250ml", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(
                text = "MEAL SCHEDULE & RECIPES",
                color = TextLightGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        // Individual Meals (Breakfast, Lunch, Evening Snack, Dinner)
        items(meals) { meal ->
            MealCardItem(
                meal = meal,
                onToggle = { onToggleMeal(meal.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun DietMacroItem(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF090D18))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun MealCardItem(
    meal: MealItem,
    onToggle: () -> Unit
) {
    val borderColor = if (meal.isCompleted) NeonGreen.copy(alpha = 0.7f) else FitCardBorderNeon.copy(alpha = 0.35f)

    NeonGlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = borderColor,
        cornerRadius = 18.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = meal.mealType.uppercase(),
                            color = NeonGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${meal.calories} kcal",
                        color = TextMutedGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Checkbox toggle button
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (meal.isCompleted) NeonGreen else Color.Transparent)
                        .border(1.5.dp, if (meal.isCompleted) NeonGreen else FitCardBorder, CircleShape)
                        .clickable { onToggle() }
                        .testTag("toggle_meal_${meal.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    if (meal.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = FitBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = meal.name,
                color = TextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = meal.hindiTitle,
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = meal.description,
                color = TextMutedGray,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Macro details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SmallNutrientChip(label = "Protein", value = "${meal.protein}g", color = NeonPurple)
                SmallNutrientChip(label = "Carbs", value = "${meal.carbs}g", color = NeonCyan)
                SmallNutrientChip(label = "Fats", value = "${meal.fats}g", color = NeonOrange)
            }
        }
    }
}

@Composable
private fun SmallNutrientChip(label: String, value: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF0A0E18))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$label: ", color = TextDarkGray, fontSize = 10.sp)
            Text(text = value, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}
