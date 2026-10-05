package com.example.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleGlow
import com.example.ui.theme.TextWhite
import java.io.File
import java.io.FileOutputStream

enum class CoachPose(
    val title: String,
    val primaryMuscle: String
) {
    IDLE("Athletic Stance", "Full Body"),
    PUSH_UP("Push Up", "Chest & Triceps"),
    SQUAT("Squat", "Quads & Glutes"),
    BICEPS_CURL("Biceps Curl", "Biceps"),
    SHOULDER_PRESS("Shoulder Press", "Deltoids"),
    LUNGES("Lunges", "Quads & Hamstrings"),
    PLANK("Plank", "Core & Abs"),
    RUNNING("Sprint / Run", "Cardio & Stamina"),
    MEDITATION("Recovery Zen", "Mindfulness"),
    CHEST_FLY("Chest Press", "Pectorals"),
    DEADLIFT("Deadlift", "Back & Hamstrings"),
    HIIT("HIIT Interval", "Cardio Endurance")
}

fun getExercisePose(exerciseName: String, categoryName: String = ""): CoachPose {
    val name = exerciseName.lowercase()
    val cat = categoryName.lowercase()
    return when {
        name.contains("push up") || name.contains("pushup") || name.contains("dip") -> CoachPose.PUSH_UP
        name.contains("squat") || name.contains("leg press") -> CoachPose.SQUAT
        name.contains("curl") -> CoachPose.BICEPS_CURL
        name.contains("shoulder") || name.contains("overhead") || name.contains("military") || name.contains("lateral raise") -> CoachPose.SHOULDER_PRESS
        name.contains("lunge") || name.contains("split squat") || name.contains("step up") -> CoachPose.LUNGES
        name.contains("plank") || name.contains("crunch") || name.contains("ab") || cat.contains("abs") || cat.contains("core") -> CoachPose.PLANK
        name.contains("run") || name.contains("sprint") || name.contains("treadmill") || cat.contains("running") || cat.contains("cardio") -> CoachPose.RUNNING
        name.contains("meditat") || name.contains("breath") || name.contains("stretch") || cat.contains("meditation") || cat.contains("recovery") -> CoachPose.MEDITATION
        name.contains("bench") || name.contains("chest") || cat.contains("chest") -> CoachPose.CHEST_FLY
        name.contains("deadlift") || name.contains("row") || name.contains("pull") || cat.contains("back") -> CoachPose.DEADLIFT
        name.contains("burpee") || name.contains("jumping") || cat.contains("hiit") -> CoachPose.HIIT
        cat.contains("bicep") -> CoachPose.BICEPS_CURL
        cat.contains("shoulder") -> CoachPose.SHOULDER_PRESS
        cat.contains("leg") -> CoachPose.SQUAT
        else -> CoachPose.IDLE
    }
}

object CoachCharacterManager {
    private const val FILE_NAME = "coach_character.png"

    fun getCoachImageFile(context: Context): File {
        return File(context.filesDir, FILE_NAME)
    }

    fun hasCoachImage(context: Context): Boolean {
        return true // Guaranteed built-in R.drawable.coach_character or user-picked file
    }

    fun getCoachImageModel(context: Context): Any {
        val file = getCoachImageFile(context)
        if (file.exists() && file.length() > 0) {
            return file
        }
        return R.drawable.coach_character
    }

    fun saveCoachImage(context: Context, uri: Uri): Boolean {
        return try {
            val file = getCoachImageFile(context)
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

/**
 * Reusable Coach Shubham Character Image Composable with soft purple neon glow border.
 * If user taps (when allowPicker = true), it opens Android Photo Picker to pick the character image.
 */
@Composable
fun CoachCharacterImage(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    contentScale: ContentScale = ContentScale.Crop,
    borderColor: Color = NeonPurpleGlow,
    borderWidth: Dp = 1.5.dp,
    allowPicker: Boolean = false,
    showPickerIconBadge: Boolean = false,
    pose: CoachPose? = null,
    showPoseBadge: Boolean = false,
    customBadgeText: String? = null,
    onImageSelected: () -> Unit = {}
) {
    val context = LocalContext.current
    var refreshKey by remember { mutableStateOf(System.currentTimeMillis()) }
    var hasCustomImage by remember(refreshKey) { mutableStateOf(CoachCharacterManager.hasCoachImage(context)) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val success = CoachCharacterManager.saveCoachImage(context, uri)
            if (success) {
                refreshKey = System.currentTimeMillis()
                hasCustomImage = true
                onImageSelected()
            }
        }
    }

    val clickableModifier = if (allowPicker) {
        Modifier.clickable {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.radialGradient(
                    listOf(NeonPurple.copy(alpha = 0.35f), Color(0xFF090D18))
                )
            )
            .border(borderWidth, borderColor, shape)
            .then(clickableModifier),
        contentAlignment = Alignment.Center
    ) {
        if (hasCustomImage) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(CoachCharacterManager.getCoachImageModel(context))
                    .crossfade(true)
                    .build(),
                contentDescription = "Coach Shubham Character",
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Fallback character avatar
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Coach Shubham",
                tint = NeonPurpleGlow,
                modifier = Modifier.size(36.dp)
            )
        }

        // Exercise Pose Badge Overlay
        val badgeLabel = customBadgeText ?: pose?.title
        if (showPoseBadge && badgeLabel != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp, start = 4.dp, end = 4.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xE6090D18))
                    .border(1.dp, NeonPurpleGlow.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeLabel.uppercase(),
                    color = NeonCyan,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (showPickerIconBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF090D18))
                    .border(1.dp, NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Change Character Photo",
                    tint = NeonCyan,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
