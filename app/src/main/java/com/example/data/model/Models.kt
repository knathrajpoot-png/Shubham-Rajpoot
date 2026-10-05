package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Shubham Rajpoot",
    val mobileNumber: String = "+91 98765 43210",
    val age: Int = 25,
    val weightKg: Float = 75f,
    val heightCm: Float = 178f,
    val gender: String = "Male",
    val goal: String = "Muscle Gain",
    val isOnboardingCompleted: Boolean = false,
    val dailyCalorieTarget: Int = 2500,
    val proteinTargetGrams: Int = 160,
    val carbsTargetGrams: Int = 280,
    val fatsTargetGrams: Int = 65,
    val waterTargetLiters: Float = 3.5f,
    val streakDays: Int = 7
)

@Entity(tableName = "daily_activity")
data class DailyActivity(
    @PrimaryKey val dateString: String, // YYYY-MM-DD
    val steps: Int = 6420,
    val stepGoal: Int = 10000,
    val caloriesBurned: Int = 520,
    val calorieGoal: Int = 650,
    val waterIntakeLiters: Float = 2.4f,
    val waterGoalLiters: Float = 3.5f,
    val exerciseTimeMin: Int = 45,
    val exerciseGoalMin: Int = 60,
    val streakDays: Int = 7
)

@Entity(tableName = "run_session")
data class RunSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val distanceKm: Float,
    val durationSeconds: Long,
    val caloriesBurned: Int,
    val paceMinPerKm: Float
)

@Entity(tableName = "reminders")
data class ReminderItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "Workout", "Meal", "Meditation", "Water", "Sleep"
    val title: String,
    val timeString: String,
    val isEnabled: Boolean = true,
    val frequency: String = "Daily"
)

@Entity(tableName = "completed_workouts")
data class CompletedWorkout(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class Exercise(
    val id: String,
    val name: String,
    val category: String,
    val targetMuscles: String,
    val sets: Int,
    val reps: String,
    val restSeconds: Int,
    val difficulty: String, // Beginner, Intermediate, Advanced
    val instructions: String,
    val safetyTips: String = "Maintain neutral spine and breathe steadily throughout movement."
)

data class WorkoutCategoryItem(
    val name: String,
    val description: String,
    val exerciseCount: Int,
    val durationMinutes: Int,
    val iconName: String,
    val exercises: List<Exercise>,
    val isSpecialtyCategory: Boolean = false
)

data class MealItem(
    val id: String,
    val mealType: String, // "Breakfast", "Lunch", "Evening Snack", "Dinner"
    val name: String,
    val hindiTitle: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val description: String,
    val isVeg: Boolean = true,
    var isCompleted: Boolean = false
)

data class AiCoachInsight(
    val title: String,
    val message: String,
    val tag: String,
    val priorityLevel: String // "High", "Normal", "Pro Tip"
)
