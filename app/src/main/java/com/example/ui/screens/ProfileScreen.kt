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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.components.CoachCharacterImage
import com.example.ui.components.GlowingGradientButton
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
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextDarkGray
import com.example.ui.theme.TextLightGray
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextWhite
import java.util.Locale

@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    onGoalChange: (String) -> Unit,
    onMeasurementsChange: (weightKg: Float, heightCm: Float) -> Unit,
    onOpenReminders: () -> Unit,
    onLogout: () -> Unit
) {
    var showGoalDialog by remember { mutableStateOf(false) }
    var showMeasurementsDialog by remember { mutableStateOf(false) }

    val name = userProfile?.name ?: "Shubham Rajpoot"
    val age = userProfile?.age ?: 25
    val weight = userProfile?.weightKg ?: 75f
    val height = userProfile?.heightCm ?: 178f
    val gender = userProfile?.gender ?: "Male"
    val goal = userProfile?.goal ?: "Muscle Gain"
    val calTarget = userProfile?.dailyCalorieTarget ?: 2500
    val proteinTarget = userProfile?.proteinTargetGrams ?: 160
    val waterTarget = userProfile?.waterTargetLiters ?: 3.5f

    val heightM = height / 100f
    val bmi = if (heightM > 0) weight / (heightM * heightM) else 23.5f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .padding(horizontal = 16.dp)
            .testTag("profile_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // User Identity & Coach Avatar Banner
            NeonGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonPurple.copy(alpha = 0.6f),
                cornerRadius = 24.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CoachCharacterImage(
                        modifier = Modifier.size(76.dp),
                        shape = CircleShape,
                        borderWidth = 1.5.dp,
                        allowPicker = true,
                        showPickerIconBadge = true
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = name,
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Coach",
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = userProfile?.mobileNumber ?: "+91 98765 43210",
                            color = TextMutedGray,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonPurple.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = goal,
                                    color = NeonPurpleGlow,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonOrange.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "7d Streak",
                                    color = NeonOrange,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // BIOMETRIC METRICS GRID
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BODY STATS & BIOMETRICS",
                    color = TextLightGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(FitCardBackground)
                        .border(1.dp, FitCardBorder, RoundedCornerShape(6.dp))
                        .clickable { showMeasurementsDialog = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("edit_metrics_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Edit", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            NeonGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = FitCardBorderNeon.copy(alpha = 0.35f),
                cornerRadius = 20.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatPill(label = "Age", value = "$age yrs", color = NeonCyan)
                        StatPill(label = "Weight", value = "${weight.toInt()} kg", color = NeonPurple)
                        StatPill(label = "Height", value = "${height.toInt()} cm", color = NeonGreen)
                        StatPill(label = "BMI", value = String.format(Locale.US, "%.1f", bmi), color = NeonOrange)
                    }
                }
            }
        }

        // CURRENT AI FITNESS PLAN RECOMMENDATIONS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PERSONALIZED AI PLAN",
                    color = TextLightGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NeonPurple.copy(alpha = 0.2f))
                        .border(1.dp, NeonPurpleGlow, RoundedCornerShape(6.dp))
                        .clickable { showGoalDialog = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("switch_goal_button")
                ) {
                    Text(text = "Change Goal", color = NeonPurpleGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            NeonGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonPurple.copy(alpha = 0.5f),
                cornerRadius = 20.dp
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PlanDetailItem(title = "Daily Calories", value = "$calTarget kcal", color = NeonOrange)
                        PlanDetailItem(title = "Protein Target", value = "${proteinTarget}g", color = NeonPurple)
                        PlanDetailItem(title = "Hydration Goal", value = "${String.format(Locale.US, "%.1f", waterTarget)}L", color = NeonCyan)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(FitCardBorder)
                    )

                    PlanRecommendationRow(
                        label = "Workout Split",
                        detail = "4-Day Upper/Lower Hypertrophy + 2 Active Zone 2 Cardio"
                    )
                    PlanRecommendationRow(
                        label = "Nutrition Split",
                        detail = "High Protein (30%) • Complex Carbohydrates (45%) • Healthy Fats (25%)"
                    )
                    PlanRecommendationRow(
                        label = "Recovery Matrix",
                        detail = "7.5 - 8 hrs sleep • 10-min evening mindfulness breathing"
                    )
                }
            }
        }

        // ACHIEVEMENTS & BADGES
        item {
            Text(
                text = "ACHIEVEMENTS & BADGES",
                color = TextLightGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BadgeCard(name = "7-Day Streak", desc = "Consistency Titan", icon = Icons.Default.Whatshot, color = NeonOrange, modifier = Modifier.weight(1f))
                BadgeCard(name = "Protein Pro", desc = "Macro Discipline", icon = Icons.Default.Bolt, color = NeonPurple, modifier = Modifier.weight(1f))
                BadgeCard(name = "Century Steps", desc = "50K+ Steps Done", icon = Icons.Default.MilitaryTech, color = NeonCyan, modifier = Modifier.weight(1f))
            }
        }

        // QUICK LINKS (Reminders, Settings, Logout)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NeonGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = FitCardBorder,
                    cornerRadius = 16.dp,
                    onClick = onOpenReminders
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Smart Reminders & Alarms", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(text = "Manage", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Logout Button
                NeonGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonRed.copy(alpha = 0.4f),
                    cornerRadius = 16.dp,
                    onClick = onLogout
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = NeonRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Reset Profile & Logout", color = NeonRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    // Change Goal Dialog
    if (showGoalDialog) {
        val goals = listOf("Muscle Gain", "Weight Loss", "Fat Loss", "Strength", "General Fitness", "Bodybuilding", "Endurance")
        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            containerColor = Color(0xFF0F1524),
            title = {
                Text(text = "Switch Fitness Goal", color = TextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    goals.forEach { g ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (g == goal) NeonPurple.copy(alpha = 0.25f) else Color(0xFF141C30))
                                .border(1.dp, if (g == goal) NeonPurpleGlow else FitCardBorder, RoundedCornerShape(10.dp))
                                .clickable {
                                    onGoalChange(g)
                                    showGoalDialog = false
                                }
                                .padding(12.dp)
                        ) {
                            Text(text = g, color = TextWhite, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGoalDialog = false }) {
                    Text(text = "Close", color = NeonCyan)
                }
            }
        )
    }

    // Edit Measurements Dialog
    if (showMeasurementsDialog) {
        var newWeight by remember { mutableStateOf(weight.toString()) }
        var newHeight by remember { mutableStateOf(height.toString()) }

        AlertDialog(
            onDismissRequest = { showMeasurementsDialog = false },
            containerColor = Color(0xFF0F1524),
            title = {
                Text(text = "Update Body Measurements", color = TextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newWeight,
                        onValueChange = { newWeight = it },
                        label = { Text("Weight (kg)") }
                    )
                    OutlinedTextField(
                        value = newHeight,
                        onValueChange = { newHeight = it },
                        label = { Text("Height (cm)") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val w = newWeight.toFloatOrNull() ?: weight
                        val h = newHeight.toFloatOrNull() ?: height
                        onMeasurementsChange(w, h)
                        showMeasurementsDialog = false
                    }
                ) {
                    Text(text = "Save & Recalculate", color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMeasurementsDialog = false }) {
                    Text(text = "Cancel", color = TextMutedGray)
                }
            }
        )
    }
}

@Composable
private fun StatPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PlanDetailItem(title: String, value: String, color: Color) {
    Column {
        Text(text = title, color = TextDarkGray, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = color, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
private fun PlanRecommendationRow(label: String, detail: String) {
    Column {
        Text(text = label, color = NeonPurpleGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = detail, color = TextLightGray, fontSize = 12.sp, lineHeight = 16.sp)
    }
}

@Composable
private fun BadgeCard(name: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier) {
    NeonGlassCard(
        modifier = modifier,
        borderColor = color.copy(alpha = 0.4f),
        cornerRadius = 16.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = name, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = desc, color = TextDarkGray, fontSize = 9.sp, maxLines = 1)
        }
    }
}
