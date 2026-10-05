package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleGlow
import com.example.ui.theme.TextDarkGray
import com.example.ui.theme.TextLightGray
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextWhite

@Composable
fun ProfileSetupScreen(
    currentStep: Int, // 1..4
    name: String,
    age: String,
    weight: String,
    height: String,
    gender: String,
    goal: String,
    isGeneratingPlan: Boolean,
    planGenerationStepText: String,
    onNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onGoalChange: (String) -> Unit,
    onNextStep: () -> Unit,
    onPrevStep: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .testTag("profile_setup_screen")
    ) {
        if (isGeneratingPlan || currentStep == 4) {
            // STEP 4: AI PLAN GENERATION ANIMATION
            PlanGenerationScreen(generationStep = planGenerationStepText)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Row: Back button (if step > 1) & Step Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (currentStep > 1) {
                        IconButton(
                            onClick = onPrevStep,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(FitCardBackground)
                                .border(1.dp, FitCardBorder, RoundedCornerShape(12.dp))
                                .testTag("setup_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextWhite
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(40.dp))
                    }

                    // Step Indicator: Step 1 of 4
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(NeonPurple.copy(alpha = 0.15f))
                            .border(1.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Step $currentStep of 4",
                            color = NeonPurpleGlow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Spacer(modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { currentStep / 4f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = NeonPurpleGlow,
                    trackColor = Color(0xFF141A29),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3D Master Coach Shubham Welcoming Avatar
                CoachCharacterImage(
                    modifier = Modifier.size(85.dp, 110.dp),
                    shape = RoundedCornerShape(20.dp),
                    borderColor = NeonPurpleGlow,
                    borderWidth = 1.5.dp,
                    pose = CoachPose.IDLE,
                    showPoseBadge = true,
                    customBadgeText = "COACH SHUBHAM",
                    allowPicker = true,
                    showPickerIconBadge = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Heading
                Text(
                    text = "Let's Build Your Fitness Profile",
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Coach Shubham needs these metrics to craft your AI plan",
                    color = TextMutedGray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // STEP CONTENT SWITCHER
                NeonGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 24.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        when (currentStep) {
                            1 -> {
                                // Step 1: Name & Gender
                                Text(
                                    text = "1. Full Name",
                                    color = TextLightGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                SetupInputField(
                                    value = name,
                                    onValueChange = onNameChange,
                                    placeholder = "Shubham Rajpoot",
                                    icon = Icons.Default.Person,
                                    testTag = "name_input"
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "5. Gender",
                                    color = TextLightGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    listOf("Male", "Female", "Other").forEach { opt ->
                                        val isSelected = gender.equals(opt, ignoreCase = true)
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(50.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    if (isSelected) NeonPurple.copy(alpha = 0.25f) else Color(0xFF090D17)
                                                )
                                                .border(
                                                    1.5.dp,
                                                    if (isSelected) NeonPurpleGlow else FitCardBorder,
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .clickable { onGenderChange(opt) }
                                                .testTag("gender_$opt"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = opt,
                                                color = if (isSelected) TextWhite else TextMutedGray,
                                                fontSize = 14.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }

                            2 -> {
                                // Step 2: Age, Weight (kg), Height (cm)
                                Text(
                                    text = "2. Age",
                                    color = TextLightGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                SetupInputField(
                                    value = age,
                                    onValueChange = onAgeChange,
                                    placeholder = "25",
                                    unitSuffix = "years",
                                    keyboardType = KeyboardType.Number,
                                    testTag = "age_input"
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = "3. Weight (kg)",
                                    color = TextLightGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                SetupInputField(
                                    value = weight,
                                    onValueChange = onWeightChange,
                                    placeholder = "75",
                                    unitSuffix = "kg",
                                    icon = Icons.Default.MonitorWeight,
                                    keyboardType = KeyboardType.Decimal,
                                    testTag = "weight_input"
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = "4. Height (cm)",
                                    color = TextLightGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                SetupInputField(
                                    value = height,
                                    onValueChange = onHeightChange,
                                    placeholder = "178",
                                    unitSuffix = "cm",
                                    icon = Icons.Default.Height,
                                    keyboardType = KeyboardType.Decimal,
                                    testTag = "height_input"
                                )

                                // Live BMI indicator
                                val wF = weight.toFloatOrNull() ?: 0f
                                val hF = (height.toFloatOrNull() ?: 0f) / 100f
                                if (wF > 0 && hF > 0) {
                                    val bmi = wF / (hF * hF)
                                    val bmiCategory = when {
                                        bmi < 18.5 -> "Underweight"
                                        bmi < 25.0 -> "Optimal Fitness"
                                        bmi < 30.0 -> "Overweight"
                                        else -> "Obese"
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(NeonCyan.copy(alpha = 0.12f))
                                            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                            .padding(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Calculated BMI: ${String.format("%.1f", bmi)}",
                                                color = TextWhite,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = bmiCategory,
                                                color = NeonCyan,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            3 -> {
                                // Step 3: Fitness Goal
                                Text(
                                    text = "6. Primary Fitness Goal",
                                    color = TextLightGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Your daily calories, macros, and workout routine will adapt to this goal.",
                                    color = TextMutedGray,
                                    fontSize = 12.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val goals = listOf(
                                    "Muscle Gain" to "Build lean muscular hypertrophy & thickness",
                                    "Weight Loss" to "Burn body fat with sustained caloric deficit",
                                    "Fat Loss" to "Retain muscle while targeting stubborn abdominal fat",
                                    "Strength" to "Boost 1RM power in compound movements",
                                    "General Fitness" to "Daily vitality, functional movement & stamina",
                                    "Bodybuilding" to "Maximum aesthetic symmetry & peak conditioning",
                                    "Endurance" to "Cardio stamina, running capacity & VO2 max"
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    goals.forEach { (gTitle, gDesc) ->
                                        val isSelected = goal.equals(gTitle, ignoreCase = true)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(
                                                    if (isSelected) NeonPurple.copy(alpha = 0.22f) else Color(0xFF090D17)
                                                )
                                                .border(
                                                    1.5.dp,
                                                    if (isSelected) NeonPurpleGlow else FitCardBorder,
                                                    RoundedCornerShape(14.dp)
                                                )
                                                .clickable { onGoalChange(gTitle) }
                                                .padding(14.dp)
                                                .testTag("goal_${gTitle.replace(" ", "_").lowercase()}"),
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = gTitle,
                                                        color = if (isSelected) TextWhite else TextLightGray,
                                                        fontSize = 15.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = gDesc,
                                                        color = if (isSelected) NeonPurpleGlow else TextDarkGray,
                                                        fontSize = 11.sp
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(CircleShape)
                                                        .border(
                                                            1.5.dp,
                                                            if (isSelected) NeonPurpleGlow else FitCardBorder,
                                                            CircleShape
                                                        )
                                                        .background(if (isSelected) NeonPurple else Color.Transparent),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (isSelected) {
                                                        Icon(
                                                            imageVector = Icons.Default.Check,
                                                            contentDescription = "Selected",
                                                            tint = TextWhite,
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Continue or "Create My Fitness Plan" Button
                        val buttonText = if (currentStep == 3) "Create My Fitness Plan" else "Continue"
                        GlowingGradientButton(
                            text = buttonText,
                            modifier = Modifier.fillMaxWidth(),
                            icon = if (currentStep == 3) Icons.Default.AutoAwesome else Icons.AutoMirrored.Filled.ArrowForward,
                            testTag = "setup_continue_button",
                            onClick = onNextStep
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SetupInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    unitSuffix: String? = null,
    icon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF090D17))
            .border(1.2.dp, FitCardBorderNeon.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NeonPurpleGlow,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .testTag(testTag),
            textStyle = TextStyle(
                color = TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            cursorBrush = SolidColor(NeonCyan),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = TextDarkGray,
                        fontSize = 15.sp
                    )
                }
                innerTextField()
            }
        )

        if (unitSuffix != null) {
            Text(
                text = unitSuffix,
                color = NeonCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PlanGenerationScreen(generationStep: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(NeonPurple.copy(alpha = 0.35f), Color(0xFF090D18))
                        )
                    )
                    .border(2.dp, NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Plan",
                    tint = NeonCyan,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Creating Your Personalized Fitness Plan...",
                color = TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            CircularProgressIndicator(
                color = NeonCyan,
                trackColor = Color(0xFF1E1738),
                strokeWidth = 3.5.dp,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = generationStep,
                color = NeonPurpleGlow,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Calibrating workouts, nutrition & recovery matrix for Shubham Rajpoot",
                color = TextDarkGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
