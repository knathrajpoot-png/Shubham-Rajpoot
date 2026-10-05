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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exercise
import com.example.ui.components.CircularNeonProgress
import com.example.ui.components.CoachCharacterImage
import com.example.ui.components.CoachPose
import com.example.ui.components.GlowingGradientButton
import com.example.ui.components.NeonGlassCard
import com.example.ui.components.getExercisePose
import com.example.ui.theme.FitBlack
import com.example.ui.theme.FitCardBackground
import com.example.ui.theme.FitCardBorder
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
fun ActiveWorkoutScreen(
    categoryName: String,
    exercises: List<Exercise>,
    currentExerciseIndex: Int,
    currentSet: Int,
    isResting: Boolean,
    restSecondsRemaining: Int,
    onCompleteSet: (totalSets: Int, restSecs: Int) -> Unit,
    onSkipRest: () -> Unit,
    onNextExercise: () -> Unit,
    onPrevExercise: () -> Unit,
    onFinishWorkout: () -> Unit,
    onExitWorkout: () -> Unit
) {
    val scrollState = rememberScrollState()
    val currentExercise = exercises.getOrNull(currentExerciseIndex) ?: exercises.first()
    val totalExercises = exercises.size
    val totalSets = currentExercise.sets

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("active_workout_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar: Exit button & Routine title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onExitWorkout,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FitCardBackground)
                        .border(1.dp, FitCardBorder, RoundedCornerShape(12.dp))
                        .testTag("exit_workout_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Workout",
                        tint = TextWhite
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$categoryName Workout",
                        color = TextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Exercise ${currentExerciseIndex + 1} of $totalExercises",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Finish Workout early button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeonGreen.copy(alpha = 0.15f))
                        .border(1.dp, NeonGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .clickable { onFinishWorkout() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("finish_workout_button")
                ) {
                    Text(
                        text = "Finish",
                        color = NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Overall Progress Bar
            LinearProgressIndicator(
                progress = { (currentExerciseIndex + 1) / totalExercises.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = NeonPurpleGlow,
                trackColor = Color(0xFF141A29),
            )

            Spacer(modifier = Modifier.height(20.dp))

            // REST MODE vs ACTIVE SET MODE
            if (isResting) {
                // REST TIMER VIEW
                NeonGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonCyan.copy(alpha = 0.7f),
                    cornerRadius = 24.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "REST & RECOVER",
                            color = NeonCyan,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CoachCharacterImage(
                                modifier = Modifier.size(90.dp, 120.dp),
                                shape = RoundedCornerShape(18.dp),
                                borderColor = NeonCyan,
                                pose = CoachPose.MEDITATION,
                                showPoseBadge = true,
                                customBadgeText = "RECOVERY",
                                allowPicker = false
                            )

                            // Circular Countdown Timer
                            CircularNeonProgress(
                                progressPercent = ((restSecondsRemaining / 45f) * 100).toInt().coerceIn(0, 100),
                                size = 125.dp,
                                strokeWidth = 10.dp,
                                accentColor = NeonCyan
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${restSecondsRemaining}s",
                                        color = TextWhite,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "Remaining",
                                        color = TextMutedGray,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Next: Set $currentSet of ${currentExercise.name}",
                            color = TextLightGray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Skip Rest Button
                        GlowingGradientButton(
                            text = "Skip Rest",
                            icon = Icons.Default.FastForward,
                            gradient = Brush.horizontalGradient(listOf(Color(0xFF0891B2), Color(0xFF00F0FF))),
                            testTag = "skip_rest_button",
                            onClick = onSkipRest
                        )
                    }
                }
            } else {
                // ACTIVE EXERCISE DEMONSTRATION & SETS VIEW
                NeonGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonPurple.copy(alpha = 0.6f),
                    cornerRadius = 24.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 3D Master Coach Shubham Active Demonstration Character
                        CoachCharacterImage(
                            modifier = Modifier.size(130.dp, 165.dp),
                            shape = RoundedCornerShape(22.dp),
                            borderColor = NeonPurpleGlow,
                            pose = getExercisePose(currentExercise.name, categoryName),
                            showPoseBadge = true,
                            allowPicker = true,
                            showPickerIconBadge = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentExercise.name,
                            color = TextWhite,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Target: ${currentExercise.targetMuscles}",
                            color = NeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Sets & Target Reps Highlight
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NeonPurple.copy(alpha = 0.2f))
                                    .border(1.dp, NeonPurpleGlow, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "SET $currentSet OF $totalSets",
                                        color = NeonPurpleGlow,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = currentExercise.reps,
                                        color = TextWhite,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Coaching tip
                        Text(
                            text = currentExercise.instructions,
                            color = TextLightGray,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Complete Set Action
                        GlowingGradientButton(
                            text = if (currentSet < totalSets) "Complete Set $currentSet" else "Complete Exercise",
                            icon = Icons.Default.Check,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "complete_set_button",
                            onClick = {
                                onCompleteSet(totalSets, currentExercise.restSeconds)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Next / Previous Exercise Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentExerciseIndex > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(FitCardBackground)
                            .border(1.dp, FitCardBorder, RoundedCornerShape(10.dp))
                            .clickable { onPrevExercise() }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(text = "Previous Exercise", color = TextLightGray, fontSize = 12.sp)
                    }
                } else {
                    Spacer(modifier = Modifier.width(10.dp))
                }

                if (currentExerciseIndex < totalExercises - 1) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(FitCardBackground)
                            .border(1.dp, FitCardBorder, RoundedCornerShape(10.dp))
                            .clickable { onNextExercise() }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Next Exercise", color = TextLightGray, fontSize = 12.sp)
                            Icon(imageVector = Icons.Default.NavigateNext, contentDescription = null, tint = TextLightGray, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
