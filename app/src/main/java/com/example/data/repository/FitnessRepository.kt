package com.example.data.repository

import com.example.data.local.FitnessDatabase
import com.example.data.model.AiCoachInsight
import com.example.data.model.CompletedWorkout
import com.example.data.model.DailyActivity
import com.example.data.model.Exercise
import com.example.data.model.MealItem
import com.example.data.model.ReminderItem
import com.example.data.model.RunSession
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutCategoryItem
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class FitnessRepository(private val db: FitnessDatabase) {

    private val profileDao = db.userProfileDao()
    private val activityDao = db.dailyActivityDao()
    private val runDao = db.runSessionDao()
    private val reminderDao = db.reminderDao()
    private val workoutDao = db.completedWorkoutDao()

    val userProfile: Flow<UserProfile?> = profileDao.getUserProfile()
    val allRuns: Flow<List<RunSession>> = runDao.getAllRuns()
    val allReminders: Flow<List<ReminderItem>> = reminderDao.getAllReminders()
    val recentWorkouts: Flow<List<CompletedWorkout>> = workoutDao.getAllWorkouts()

    fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun getTodayActivity(): Flow<DailyActivity?> {
        return activityDao.getActivityForDate(getTodayDateString())
    }

    suspend fun ensureInitialData() {
        val today = getTodayDateString()
        val existingAct = activityDao.getActivityForDateSync(today)
        if (existingAct == null) {
            activityDao.insertOrUpdate(
                DailyActivity(
                    dateString = today,
                    steps = 6420,
                    stepGoal = 10000,
                    caloriesBurned = 520,
                    calorieGoal = 650,
                    waterIntakeLiters = 2.4f,
                    waterGoalLiters = 3.5f,
                    exerciseTimeMin = 45,
                    exerciseGoalMin = 60,
                    streakDays = 7
                )
            )
        }

        // Default reminders if empty
        val existingReminders = db.reminderDao().getAllReminders()
        // Check if reminders table is populated
        // Insert sample defaults
        val defaultReminders = listOf(
            ReminderItem(type = "Workout", title = "Morning Power Workout", timeString = "07:00 AM", isEnabled = true),
            ReminderItem(type = "Meal", title = "High-Protein Lunch", timeString = "01:00 PM", isEnabled = true),
            ReminderItem(type = "Water", title = "Hydration Check", timeString = "Every 1 Hour", isEnabled = true),
            ReminderItem(type = "Meditation", title = "Mindfulness Reset", timeString = "08:00 PM", isEnabled = true),
            ReminderItem(type = "Sleep", title = "Recovery Sleep", timeString = "10:30 PM", isEnabled = true)
        )
        for (item in defaultReminders) {
            reminderDao.insertReminder(item)
        }
    }

    suspend fun saveProfileAndGeneratePlan(
        name: String,
        age: Int,
        weightKg: Float,
        heightCm: Float,
        gender: String,
        goal: String,
        mobileNumber: String = "+91 98765 43210"
    ): UserProfile {
        // Mifflin-St Jeor formula for BMR
        val isMale = gender.equals("Male", ignoreCase = true)
        val s = if (isMale) 5 else -161
        val bmr = (10 * weightKg) + (6.25f * heightCm) - (5 * age) + s
        val tdee = bmr * 1.45f

        val (targetCal, proteinG, carbsG, fatG) = when (goal) {
            "Muscle Gain" -> {
                val cal = (tdee + 350).roundToInt()
                val protein = (weightKg * 2.0f).roundToInt()
                val fat = (weightKg * 0.9f).roundToInt()
                val carbs = ((cal - (protein * 4) - (fat * 9)) / 4).coerceAtLeast(150)
                Quad(cal, protein, carbs, fat)
            }
            "Weight Loss" -> {
                val cal = (tdee - 450).coerceAtLeast(1500f).roundToInt()
                val protein = (weightKg * 1.8f).roundToInt()
                val fat = (weightKg * 0.7f).roundToInt()
                val carbs = ((cal - (protein * 4) - (fat * 9)) / 4).coerceAtLeast(120)
                Quad(cal, protein, carbs, fat)
            }
            "Fat Loss" -> {
                val cal = (tdee - 400).coerceAtLeast(1600f).roundToInt()
                val protein = (weightKg * 2.1f).roundToInt()
                val fat = (weightKg * 0.8f).roundToInt()
                val carbs = ((cal - (protein * 4) - (fat * 9)) / 4).coerceAtLeast(110)
                Quad(cal, protein, carbs, fat)
            }
            "Strength" -> {
                val cal = (tdee + 250).roundToInt()
                val protein = (weightKg * 2.0f).roundToInt()
                val fat = (weightKg * 1.0f).roundToInt()
                val carbs = ((cal - (protein * 4) - (fat * 9)) / 4).coerceAtLeast(160)
                Quad(cal, protein, carbs, fat)
            }
            "Bodybuilding" -> {
                val cal = (tdee + 400).roundToInt()
                val protein = (weightKg * 2.2f).roundToInt()
                val fat = (weightKg * 0.9f).roundToInt()
                val carbs = ((cal - (protein * 4) - (fat * 9)) / 4).coerceAtLeast(180)
                Quad(cal, protein, carbs, fat)
            }
            "Endurance" -> {
                val cal = (tdee + 200).roundToInt()
                val protein = (weightKg * 1.6f).roundToInt()
                val fat = (weightKg * 0.8f).roundToInt()
                val carbs = ((cal - (protein * 4) - (fat * 9)) / 4).coerceAtLeast(250)
                Quad(cal, protein, carbs, fat)
            }
            else -> { // General Fitness
                val cal = tdee.roundToInt()
                val protein = (weightKg * 1.5f).roundToInt()
                val fat = (weightKg * 0.8f).roundToInt()
                val carbs = ((cal - (protein * 4) - (fat * 9)) / 4).coerceAtLeast(150)
                Quad(cal, protein, carbs, fat)
            }
        }

        val targetWater = ((weightKg * 0.04f) * 10f).roundToInt() / 10f

        val profile = UserProfile(
            id = 1,
            name = name.ifBlank { "Shubham Rajpoot" },
            mobileNumber = mobileNumber,
            age = age,
            weightKg = weightKg,
            heightCm = heightCm,
            gender = gender,
            goal = goal,
            isOnboardingCompleted = true,
            dailyCalorieTarget = targetCal,
            proteinTargetGrams = proteinG,
            carbsTargetGrams = carbsG,
            fatsTargetGrams = fatG,
            waterTargetLiters = targetWater,
            streakDays = 7
        )

        profileDao.insertOrUpdate(profile)
        return profile
    }

    private data class Quad(val a: Int, val b: Int, val c: Int, val d: Int)

    suspend fun addWaterQuick(amountLiters: Float = 0.25f) {
        val today = getTodayDateString()
        activityDao.addWater(today, amountLiters)
    }

    suspend fun addStepsQuick(steps: Int = 500) {
        val today = getTodayDateString()
        val cals = (steps * 0.04f).roundToInt()
        activityDao.addSteps(today, steps, cals)
    }

    suspend fun logCompletedWorkout(title: String, category: String, durationMinutes: Int, calories: Int) {
        workoutDao.insertWorkout(
            CompletedWorkout(
                title = title,
                category = category,
                durationMinutes = durationMinutes,
                caloriesBurned = calories
            )
        )
        val today = getTodayDateString()
        val currentAct = activityDao.getActivityForDateSync(today)
        if (currentAct != null) {
            val updated = currentAct.copy(
                caloriesBurned = currentAct.caloriesBurned + calories,
                exerciseTimeMin = currentAct.exerciseTimeMin + durationMinutes
            )
            activityDao.insertOrUpdate(updated)
        }
    }

    suspend fun saveRunSession(distanceKm: Float, durationSeconds: Long, caloriesBurned: Int, paceMinPerKm: Float) {
        runDao.insertRun(
            RunSession(
                distanceKm = distanceKm,
                durationSeconds = durationSeconds,
                caloriesBurned = caloriesBurned,
                paceMinPerKm = paceMinPerKm
            )
        )
        val today = getTodayDateString()
        val currentAct = activityDao.getActivityForDateSync(today)
        if (currentAct != null) {
            val runSteps = (distanceKm * 1300).roundToInt()
            val updated = currentAct.copy(
                steps = currentAct.steps + runSteps,
                caloriesBurned = currentAct.caloriesBurned + caloriesBurned,
                exerciseTimeMin = currentAct.exerciseTimeMin + (durationSeconds / 60).toInt()
            )
            activityDao.insertOrUpdate(updated)
        }
    }

    suspend fun toggleReminder(reminder: ReminderItem) {
        reminderDao.updateReminder(reminder.copy(isEnabled = !reminder.isEnabled))
    }

    suspend fun addReminder(reminder: ReminderItem) {
        reminderDao.insertReminder(reminder)
    }

    suspend fun deleteReminder(id: Long) {
        reminderDao.deleteReminder(id)
    }

    fun getAiCoachSuggestions(profile: UserProfile?, activity: DailyActivity?): List<AiCoachInsight> {
        val goal = profile?.goal ?: "Muscle Gain"
        val water = activity?.waterIntakeLiters ?: 2.4f
        val waterGoal = profile?.waterTargetLiters ?: 3.5f
        val steps = activity?.steps ?: 6420

        val list = mutableListOf<AiCoachInsight>()

        when (goal) {
            "Muscle Gain", "Bodybuilding" -> {
                list.add(
                    AiCoachInsight(
                        title = "Protein Timing Optimization",
                        message = "Ensure you consume 30-40g of clean protein within 90 minutes post-workout for peak muscle protein synthesis.",
                        tag = "Nutrition",
                        priorityLevel = "High"
                    )
                )
                list.add(
                    AiCoachInsight(
                        title = "Progressive Overload",
                        message = "Aim to add 1 rep or 1.5kg on your key compound lifts this week. Consistency beats intensity!",
                        tag = "Workout",
                        priorityLevel = "Pro Tip"
                    )
                )
            }
            "Weight Loss", "Fat Loss" -> {
                list.add(
                    AiCoachInsight(
                        title = "Caloric Deficit Guard",
                        message = "You are on track! Prioritize fiber-rich vegetables with lunch to maintain satiety without extra calories.",
                        tag = "Diet",
                        priorityLevel = "High"
                    )
                )
                list.add(
                    AiCoachInsight(
                        title = "NEAT Boost",
                        message = "Take a brisk 10-minute walk after meals. It stabilizes blood glucose and adds 1,200 effortless steps.",
                        tag = "Activity",
                        priorityLevel = "Pro Tip"
                    )
                )
            }
            else -> {
                list.add(
                    AiCoachInsight(
                        title = "Cardiovascular Health",
                        message = "Your heart rate recovery has improved. Keep alternating compound strength days with aerobic zone 2 sessions.",
                        tag = "Recovery",
                        priorityLevel = "Normal"
                    )
                )
            }
        }

        if (water < waterGoal) {
            list.add(
                AiCoachInsight(
                    title = "Hydration Alert",
                    message = "You are ${(waterGoal - water).let { String.format(Locale.US, "%.1f", it) }}L away from your optimal hydration goal. Drink a tall glass now!",
                    tag = "Water",
                    priorityLevel = "High"
                )
            )
        }

        if (steps >= 6000) {
            list.add(
                AiCoachInsight(
                    title = "Daily Momentum",
                    message = "Great pace today! You've crossed 6,400 steps. Complete an evening walk to smash the 10,000 mark.",
                    tag = "Streak",
                    priorityLevel = "Normal"
                )
            )
        }

        return list
    }

    fun getWorkoutCategories(): List<WorkoutCategoryItem> {
        return listOf(
            WorkoutCategoryItem(
                name = "Chest",
                description = "Build upper body pushing power and pectoral thickness",
                exerciseCount = 6,
                durationMinutes = 40,
                iconName = "chest",
                exercises = listOf(
                    Exercise("c1", "Standard Push Ups", "Chest", "Pectorals, Anterior Deltoids, Triceps", 3, "15-20 reps", 45, "Beginner", "Keep core engaged, lower chest until 1 inch off floor, push up explosively."),
                    Exercise("c2", "Incline Push Ups", "Chest", "Lower Chest, Triceps", 3, "12-15 reps", 45, "Beginner", "Place hands on bench or elevated surface to target lower pectoral fiber."),
                    Exercise("c3", "Diamond Push Ups", "Chest", "Inner Chest, Triceps", 3, "10-12 reps", 60, "Intermediate", "Form a diamond shape with index fingers and thumbs under center of chest."),
                    Exercise("c4", "Dumbbell Floor Press", "Chest", "Mid Pectorals, Core", 4, "10-12 reps", 60, "Intermediate", "Lie flat on floor, drive dumbbells upward with control, squeezing at apex."),
                    Exercise("c5", "Wide Grip Push Ups", "Chest", "Outer Chest, Serratus", 3, "12-15 reps", 45, "Intermediate", "Position hands wider than shoulder-width to maximize chest stretch."),
                    Exercise("c6", "Chest Squeeze Isometric", "Chest", "Pectoral Clavicular", 3, "30 sec hold", 30, "Beginner", "Press palms together at chest height with maximum force.")
                )
            ),
            WorkoutCategoryItem(
                name = "Back",
                description = "V-Taper development and postural spinal strength",
                exerciseCount = 6,
                durationMinutes = 45,
                iconName = "back",
                exercises = listOf(
                    Exercise("b1", "Superman Holds", "Back", "Erector Spinae, Glutes", 3, "12 reps (3s hold)", 45, "Beginner", "Lie prone, lift chest and thighs simultaneously, engaging posterior chain."),
                    Exercise("b2", "Bent Over Dumbbell Rows", "Back", "Latissimus Dorsi, Rhomboids", 4, "12 reps", 60, "Intermediate", "Hinge at hips with flat spine, pull elbows toward hips."),
                    Exercise("b3", "Doorframe Rows", "Back", "Lats, Biceps", 3, "15 reps", 45, "Beginner", "Grip doorframe, lean back at 45 degree angle and row torso forward."),
                    Exercise("b4", "Reverse Snow Angels", "Back", "Lower Trapezius, Rhomboids", 3, "12 reps", 45, "Beginner", "Prone position, sweep arms in wide arc while keeping chest lifted."),
                    Exercise("b5", "Dumbbell Renegade Rows", "Back", "Back & Deep Core", 3, "10 reps/side", 60, "Advanced", "Hold high plank on dumbbells, row one arm up without twisting hips."),
                    Exercise("b6", "Good Mornings", "Back", "Lower Back, Hamstrings", 3, "15 reps", 45, "Beginner", "Hands behind head, hinge hips backward with slight knee bend.")
                )
            ),
            WorkoutCategoryItem(
                name = "Biceps",
                description = "Peak bicep development and forearm grip resilience",
                exerciseCount = 5,
                durationMinutes = 35,
                iconName = "biceps",
                exercises = listOf(
                    Exercise("bi1", "Dumbbell Bicep Curls", "Biceps", "Biceps Brachii", 4, "12 reps", 45, "Intermediate", "Keep elbows pinned to ribs, rotate wrists slightly upward at peak contraction."),
                    Exercise("bi2", "Hammer Curls", "Biceps", "Brachialis, Forearms", 4, "12-15 reps", 45, "Intermediate", "Palms facing each other throughout movement, creating arm thickness."),
                    Exercise("bi3", "Concentration Curls", "Biceps", "Bicep Peak", 3, "10 reps/arm", 45, "Intermediate", "Elbow braced against inner thigh for strict isolated bicep squeeze."),
                    Exercise("bi4", "Towel Grip Bodyweight Curls", "Biceps", "Biceps, Grip Strength", 3, "12 reps", 45, "Beginner", "Use sturdy door anchor and towel to pull body forward using arms."),
                    Exercise("bi5", "Zottman Curls", "Biceps", "Biceps & Forearms", 3, "10 reps", 60, "Advanced", "Curl up palms facing up, rotate at top to palms down for the eccentric.")
                )
            ),
            WorkoutCategoryItem(
                name = "Triceps",
                description = "Two-thirds of your arm size and locking pushing power",
                exerciseCount = 5,
                durationMinutes = 35,
                iconName = "triceps",
                exercises = listOf(
                    Exercise("t1", "Chair / Bench Dips", "Triceps", "Lateral & Medial Triceps", 4, "15 reps", 45, "Beginner", "Lower hips vertically keeping back close to bench edge, press upward."),
                    Exercise("t2", "Close-Grip Push Ups", "Triceps", "Triceps, Chest", 3, "12 reps", 45, "Intermediate", "Hands spaced 6 inches apart, elbows tuck tightly alongside ribcage."),
                    Exercise("t3", "Overhead Dumbbell Extension", "Triceps", "Long Head Triceps", 3, "12-15 reps", 60, "Intermediate", "Hold dumbbell vertically behind head, extend elbows to full lockout."),
                    Exercise("t4", "Tricep Kickbacks", "Triceps", "Tricep Peak", 3, "15 reps/arm", 45, "Beginner", "Torso parallel to ground, extend dumbbell back and squeeze tricep for 1 second."),
                    Exercise("t5", "Floor Skull Crushers", "Triceps", "Tricep Long Head", 3, "12 reps", 60, "Intermediate", "Lie on floor, hinge at elbows lowering weight toward forehead.")
                )
            ),
            WorkoutCategoryItem(
                name = "Shoulders",
                description = "Broad boulder deltoids and athletic posture",
                exerciseCount = 5,
                durationMinutes = 40,
                iconName = "shoulders",
                exercises = listOf(
                    Exercise("s1", "Pike Push Ups", "Shoulders", "Anterior & Lateral Delts", 3, "10-12 reps", 60, "Intermediate", "Inverted V hips high, lower head toward floor at 45 degree angle."),
                    Exercise("s2", "Dumbbell Shoulder Press", "Shoulders", "Entire Deltoid Complex", 4, "10-12 reps", 60, "Intermediate", "Press vertically from ear height without arching lower back."),
                    Exercise("s3", "Lateral Raises", "Shoulders", "Lateral Delts (Width)", 4, "15 reps", 45, "Intermediate", "Lead with elbows, raise arms to parallel, slight internal tilt."),
                    Exercise("s4", "Front Plate/Dumbbell Raises", "Shoulders", "Anterior Deltoid", 3, "12 reps", 45, "Beginner", "Raise weight to eye level with controlled tempo."),
                    Exercise("s5", "Rear Delt Flyes", "Shoulders", "Posterior Delts, Upper Back", 3, "15 reps", 45, "Beginner", "Hinged forward, fly weights outward squeezing rear shoulder blades.")
                )
            ),
            WorkoutCategoryItem(
                name = "Legs",
                description = "Foundation of power, quad definition, and calorie burning",
                exerciseCount = 6,
                durationMinutes = 50,
                iconName = "legs",
                exercises = listOf(
                    Exercise("l1", "Bodyweight Squats", "Legs", "Quadriceps, Glutes", 4, "20 reps", 45, "Beginner", "Drive knees out in line with toes, hips sink below parallel."),
                    Exercise("l2", "Walking Lunges", "Legs", "Quads, Hamstrings, Glutes", 3, "12 steps/leg", 60, "Intermediate", "Step forward, drop back knee to hover 1 inch above floor."),
                    Exercise("l3", "Bulgarian Split Squats", "Legs", "Glute Max, Quadriceps", 3, "10 reps/leg", 60, "Advanced", "Elevate rear foot on chair or bench, descend into single-leg squat."),
                    Exercise("l4", "Glute Bridges", "Legs", "Glutes, Hamstrings", 4, "15-20 reps", 45, "Beginner", "Drive heels into floor, elevate hips to straight diagonal, squeeze glutes."),
                    Exercise("l5", "Calf Raises", "Legs", "Gastrocnemius, Soleus", 4, "25 reps", 30, "Beginner", "Rise high on balls of feet, hold peak contraction for 1 second."),
                    Exercise("l6", "Jump Squats", "Legs", "Explosive Fast-Twitch Legs", 3, "12 reps", 60, "Intermediate", "Explode vertically from squat, land softly absorbing impact.")
                )
            ),
            WorkoutCategoryItem(
                name = "Core",
                description = "Carved six-pack abs, obliques, and rotational stability",
                exerciseCount = 6,
                durationMinutes = 30,
                iconName = "core",
                exercises = listOf(
                    Exercise("co1", "Plank", "Core", "Transverse Abdominis, Core Wall", 3, "45-60 sec hold", 45, "Beginner", "Straight line from heels to head, squeeze glutes and pull navel inward."),
                    Exercise("co2", "Mountain Climbers", "Core", "Rectus Abdominis, Hip Flexors", 3, "30 sec rapid", 45, "Intermediate", "Push-up stance, alternate driving knees toward chest at high tempo."),
                    Exercise("co3", "Bicycle Crunches", "Core", "Internal & External Obliques", 3, "20 reps total", 45, "Intermediate", "Opposite elbow to opposite knee, slow controlled rotational tempo."),
                    Exercise("co4", "Leg Raises", "Core", "Lower Abdominals", 3, "15 reps", 45, "Intermediate", "Lie flat, hands beneath hips, raise legs to 90 degrees without arching back."),
                    Exercise("co5", "Russian Twists", "Core", "Obliques, Rotational Power", 3, "20 reps total", 45, "Beginner", "V-sit position, rotate torso side to side touching ground beside hips."),
                    Exercise("co6", "Hollow Body Hold", "Core", "Deep Abdominal Wall", 3, "30 sec hold", 45, "Advanced", "Lower back pressed firmly into floor, arms and legs extended 6 inches off ground.")
                )
            ),
            WorkoutCategoryItem(
                name = "Full Body",
                description = "High-energy athletic conditioning & total muscular engagement",
                exerciseCount = 6,
                durationMinutes = 45,
                iconName = "full_body",
                exercises = listOf(
                    Exercise("f1", "Push Ups", "Chest & Arms", "Chest, Triceps, Core", 3, "15 reps", 45, "Beginner", "Full range push up, active core brace."),
                    Exercise("f2", "Squats", "Legs", "Quads, Glutes", 3, "20 reps", 45, "Beginner", "Deep hip flexion squat with chest proud."),
                    Exercise("f3", "Plank", "Core", "Total Core Endurance", 3, "60 sec", 45, "Beginner", "Solid isometric plank hold."),
                    Exercise("f4", "Lunges", "Lower Body", "Quads, Hamstrings", 3, "12 reps/leg", 45, "Intermediate", "Dynamic forward lunges."),
                    Exercise("f5", "Mountain Climbers", "Conditioning", "Core & Cardio", 3, "40 sec", 45, "Intermediate", "Dynamic athletic knee drives."),
                    Exercise("f6", "Burpees", "Full Conditioning", "Full Body Power", 3, "10-12 reps", 60, "Advanced", "Drop to floor, push up, hop feet in and explode upward with clap.")
                )
            )
        )
    }

    fun getSpecialtyWorkoutCategories(): List<WorkoutCategoryItem> {
        return listOf(
            WorkoutCategoryItem(
                name = "Mobility & Flexibility",
                description = "Joint rotational freedom, hip capsule release, and athletic fluidity",
                exerciseCount = 5,
                durationMinutes = 25,
                iconName = "mobility",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("mf1", "World's Greatest Stretch", "Mobility", "Hip Flexors, Thoracic Spine, Hamstrings", 3, "6 reps/side", 30, "Beginner", "Lunge forward, place same-side elbow inside ankle, then rotate chest toward ceiling.", "Keep the rear knee locked and breathe into the rotational thoracic stretch."),
                    Exercise("mf2", "Cat-Cow Dynamic Flow", "Mobility", "Spinal Column, Erector Spinae", 3, "12 slow cycles", 30, "Beginner", "All fours: arch back looking upward, then round spine pulling chin to chest.", "Move smoothly with inhalation and exhalation without forcing end-range."),
                    Exercise("mf3", "Thoracic Spine Windmills", "Mobility", "Mid-back, Posterior Deltoid", 3, "10 reps/side", 30, "Intermediate", "Side-lying with knees pinned, sweep top arm in large circle overhead.", "Keep knees pressed firmly to the floor to isolate upper back rotation."),
                    Exercise("mf4", "Deep Frog Stretch", "Mobility", "Adductors, Pelvic Capsule", 3, "45 sec hold", 40, "Intermediate", "Knees wide on mat, hips pushed backward toward heels with flat spine.", "Do not allow lower back to excessively arch; ease backward gently."),
                    Exercise("mf5", "90/90 Hip Capsule Rotations", "Mobility", "Hip Internal/External Rotators", 3, "8 reps/side", 30, "Intermediate", "Seated with both legs at 90-degree angles, transition smoothly without hands.", "Keep tall posture and drive hips forward through transitions.")
                )
            ),
            WorkoutCategoryItem(
                name = "Stretching",
                description = "Full body tension release, muscle lengthening & myofascial relaxation",
                exerciseCount = 5,
                durationMinutes = 20,
                iconName = "stretching",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("st1", "Standing Hamstring Fold", "Stretching", "Biceps Femoris, Lower Back", 3, "45 sec hold", 30, "Beginner", "Hinge from hips with soft knees, reach fingertips toward floor or shins.", "Breathe deeply, letting head and neck hang completely relaxed."),
                    Exercise("st2", "Cobra Abdominal Stretch", "Stretching", "Rectus Abdominis, Hip Flexors", 3, "30 sec hold", 30, "Beginner", "Lie prone, press palms into floor, extend arms gently lifting chest.", "Keep shoulders pulled away from ears and glutes relaxed."),
                    Exercise("st3", "Kneeling Quad & Hip Flexor Stretch", "Stretching", "Quadriceps, Psoas", 3, "40 sec/leg", 30, "Beginner", "Half-kneeling stance, tuck pelvis under and shift hips forward slightly.", "Squeeze the glute on the rear leg to deepen the anterior hip stretch."),
                    Exercise("st4", "Cross-Body Deltoid Stretch", "Stretching", "Posterior Shoulder, Rotator Cuff", 3, "30 sec/arm", 20, "Beginner", "Draw one arm straight across chest, hook with opposite forearm.", "Avoid twisting torso; keep chest pointing straight ahead."),
                    Exercise("st5", "Seated Butterfly Adductor Stretch", "Stretching", "Inner Thighs, Groin", 3, "45 sec hold", 30, "Beginner", "Soles of feet together, hold ankles, gently press knees downward.", "Hinge forward from hips with a flat back rather than rounding neck.")
                )
            ),
            WorkoutCategoryItem(
                name = "Glutes",
                description = "Maximum posterior chain power, glute max activation & hip drive",
                exerciseCount = 5,
                durationMinutes = 35,
                iconName = "glutes",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("gl1", "Barbell / Dumbbell Hip Thrusts", "Glutes", "Gluteus Maximus", 4, "12 reps (2s hold)", 60, "Intermediate", "Upper back on bench, drive hips up to full horizontal extension with shins vertical.", "Do not hyperextend lower back at the top; lock out through hips."),
                    Exercise("gl2", "Donkey Kicks with Iso Hold", "Glutes", "Upper Glute, Hamstring", 3, "15 reps/leg", 45, "Beginner", "Hands and knees, kick heel toward ceiling keeping knee bent at 90 degrees.", "Keep core braced to prevent pelvis from tilting or rotating."),
                    Exercise("gl3", "Fire Hydrants", "Glutes", "Gluteus Medius, Abductors", 3, "15 reps/leg", 45, "Beginner", "Lift knee outward to side keeping torso strictly stable and square to floor.", "Control the lowering phase; avoid swinging the leg with momentum."),
                    Exercise("gl4", "Single-Leg Romanian Deadlift", "Glutes", "Glutes, Hamstrings, Proprioception", 3, "10 reps/leg", 60, "Intermediate", "Hinge at hip on standing leg while extending opposite leg straight back.", "Keep hips parallel to ground; do not let the opening hip flare up."),
                    Exercise("gl5", "Lateral Banded Monster Walks", "Glutes", "Glute Medius & Minimus", 3, "20 steps/side", 45, "Beginner", "Band around knees, athletic quarter squat, step sideways with control.", "Keep constant tension on band and toes pointing forward.")
                )
            ),
            WorkoutCategoryItem(
                name = "Calves",
                description = "Diamond calf definition, Achilles tendon elasticity & explosive propulsion",
                exerciseCount = 5,
                durationMinutes = 25,
                iconName = "calves",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("ca1", "Single-Leg Elevated Calf Raise", "Calves", "Gastrocnemius", 4, "15 reps/leg", 45, "Intermediate", "Ball of foot on step, lower heel deep into stretch, explode onto big toe.", "Hold peak contraction at the top for a full 2-second squeeze."),
                    Exercise("ca2", "Seated Soleus Calf Raise", "Calves", "Soleus (Deep Calf)", 4, "20 reps", 45, "Beginner", "Seated with knees bent at 90 degrees, raise heels under resistance.", "Knee flexion takes the gastrocnemius off stretch, isolating soleus."),
                    Exercise("ca3", "Tibialis Wall Taps", "Calves", "Tibialis Anterior (Shin)", 3, "25 rapid taps", 30, "Beginner", "Back against wall, heels 1 foot out, flex toes upward toward shins.", "Builds bulletproof shins and protects against shin splints."),
                    Exercise("ca4", "Eccentric Heel Drop Protocol", "Calves", "Achilles Tendon, Lower Gastrocnemius", 3, "12 reps (4s eccentric)", 45, "Intermediate", "Raise up on both feet, remove one foot, lower down over 4 seconds.", "Essential for tendon stiffness, spring power, and ankle durability."),
                    Exercise("ca5", "Double-Under Jump Rope Skips", "Calves", "Fast-Twitch Plantarflexors", 4, "60 sec rounds", 45, "Intermediate", "Stay on balls of feet, minimal knee bend, bouncing strictly off ankles.", "Land softly with ankles flexing to absorb and return elastic energy.")
                )
            ),
            WorkoutCategoryItem(
                name = "Forearms & Grip",
                description = "Crushing grip strength, forearm thickness & wrist joint integrity",
                exerciseCount = 5,
                durationMinutes = 30,
                iconName = "forearms",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("fg1", "Dead Hang from Pullup Bar", "Forearms", "Flexor Digitorum, Grip Endurance", 4, "45-60 sec hold", 60, "Beginner", "Hang with full overhand grip, engaging lats slightly to protect shoulders.", "Keep thumbs wrapped completely around bar for active forearm recruitment."),
                    Exercise("fg2", "Palms-Up Wrist Curls", "Forearms", "Wrist Flexors", 3, "15 reps", 45, "Beginner", "Forearms supported on bench, roll weight to fingertips, curl wrists upward.", "Use controlled tempo and avoid using momentum from the elbows."),
                    Exercise("fg3", "Reverse Barbell / Dumbbell Curls", "Forearms", "Brachioradialis, Extensors", 3, "12 reps", 45, "Intermediate", "Overhand pronated grip, curl weight up without elbows drifting forward.", "Prevents golfer's elbow and thickens the upper forearm belly."),
                    Exercise("fg4", "Plate Pinch Hold", "Forearms", "Thumb Adductors, Pinch Grip", 3, "30 sec hold/side", 45, "Intermediate", "Pinch two flat weight plates together between fingers and thumb.", "Stand upright with chest tall and hold until grip gives out."),
                    Exercise("fg5", "Heavy Farmer's Walk", "Forearms", "Full Forearm Musculature, Trapezius", 4, "40 meters", 60, "Advanced", "Hold heavy weights at sides, walk in steady cadence with shoulders packed.", "Maintain rigid posture; do not allow weights to pull shoulders down.")
                )
            ),
            WorkoutCategoryItem(
                name = "Lower Back",
                description = "Spinal stability, posterior erector thickness & lumbar resilience",
                exerciseCount = 5,
                durationMinutes = 30,
                iconName = "lower_back",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("lb1", "Bird-Dog Cross Reaches", "Lower Back", "Multifidus, Erector Spinae, Core", 3, "10 reps/side (3s hold)", 45, "Beginner", "All fours: reach opposite arm and leg straight out without twisting hips.", "Imagine a glass of water resting on your lower back—keep it level."),
                    Exercise("lb2", "Jefferson Curls (Light / Bodyweight)", "Lower Back", "Posterior Chain Segmental Mobility", 3, "8 slow reps", 60, "Intermediate", "Stand on box, tuck chin to chest, roll down spine vertebra by vertebra.", "Use very light weight or bodyweight only; prioritize spinal flexion control."),
                    Exercise("lb3", "Prone Cobra Holds", "Lower Back", "Thoracolumbar Fascia, Rhomboids", 3, "10 reps (5s hold)", 45, "Beginner", "Lie face down, external rotation of thumbs toward ceiling, lift chest.", "Keep chin tucked looking at floor to keep neck neutral."),
                    Exercise("lb4", "Floor Hyperextensions", "Lower Back", "Lower Erector Spinae, Glutes", 3, "15 reps", 45, "Beginner", "Prone position, hands behind ears, lift torso 4 inches off ground.", "Squeeze glutes at top of lift to share load with lumbar muscles."),
                    Exercise("lb5", "Swimmer Kicks", "Lower Back", "Posterior Chain Rhythm", 3, "40 sec", 45, "Beginner", "Prone position, flutter opposite arm and leg continuously.", "Maintain steady breathing and keep pelvis firmly grounded.")
                )
            ),
            WorkoutCategoryItem(
                name = "Neck",
                description = "Cervical spine protection, trapezius synergy & athletic impact defense",
                exerciseCount = 5,
                durationMinutes = 20,
                iconName = "neck",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("nk1", "Isometric 4-Way Neck Holds", "Neck", "Sternocleidomastoid, Scalenes", 3, "15 sec each direction", 30, "Beginner", "Place palm on forehead, temples, and back of head; push head into hand isometrically.", "Do not bend neck; resist movement strictly with neutral posture."),
                    Exercise("nk2", "Chin Tucks (Deep Cervical Retraction)", "Neck", "Longus Colli, Deep Neck Flexors", 3, "15 reps (3s hold)", 30, "Beginner", "Stand tall, glide chin straight backward as if making a double chin.", "Reverses text neck posture and aligns cervical spine over thoracic spine."),
                    Exercise("nk3", "Cervical Spine Controlled Rotations", "Neck", "Splenius Capitis, Levator Scapulae", 3, "8 slow circles/dir", 30, "Beginner", "Slow, deliberate circular rotations tracing maximum safe range.", "Never jerk or roll head quickly; move smoothly with breath."),
                    Exercise("nk4", "Dumbbell Shrugs with Peak 3s Pause", "Neck", "Upper Trapezius, Levator Scapulae", 4, "12 reps", 45, "Intermediate", "Shrug shoulders straight up toward ears, hold peak contraction for 3 seconds.", "Do not roll shoulders backward; move strictly in vertical plane."),
                    Exercise("nk5", "Lateral Neck Tilts with Light Resistance", "Neck", "Lateral Cervical Flexors", 3, "12 reps/side", 30, "Beginner", "Tilt ear gently toward shoulder, using palm for light isometric resistance.", "Stop immediately if any sharp pain or dizziness occurs.")
                )
            ),
            WorkoutCategoryItem(
                name = "Functional Training",
                description = "Cross-planar athletic movement, kinetic chain transfers & real-world strength",
                exerciseCount = 5,
                durationMinutes = 40,
                iconName = "functional",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("fn1", "Turkish Get-Up", "Functional", "Whole Body Kinetic Chain, Shoulder Stability", 3, "5 reps/side", 60, "Advanced", "Lie on floor holding weight overhead, transition through rolling, bridge, lunge to stand.", "Keep eyes locked on the weight at all times; move through stages deliberately."),
                    Exercise("fn2", "Bear Crawl Agility Drills", "Functional", "Cross-Body Core, Serratus Anterior", 3, "20 meters", 60, "Intermediate", "Hover knees 1 inch off floor, crawl forward and backward with opposite limbs.", "Keep back completely flat like a table; do not swing hips."),
                    Exercise("fn3", "Suitcase Unilateral Carry", "Functional", "Quadratus Lumborum, Obliques, Anti-Lateral Flexion", 3, "40m/side", 60, "Intermediate", "Carry heavy dumbbell in one hand only, walk completely upright without leaning.", "Resist the pull of the weight; keep shoulders level."),
                    Exercise("fn4", "Woodchopper Cable / Dumbbell Slams", "Functional", "Rotational Core, Hip Internal Rotation", 3, "12 reps/side", 45, "Intermediate", "Drive from back hip, rotate torso diagonally downward across front knee.", "Pivot on rear foot to generate power from the hips rather than lumbar."),
                    Exercise("fn5", "Single-Arm Dumbbell Snatch", "Functional", "Explosive Triple Extension, Shoulder", 4, "8 reps/arm", 60, "Advanced", "Dumbbell on floor between feet, explode upward driving through hips straight overhead.", "Keep weight close to body like unzipping a jacket; catch with soft knees.")
                )
            ),
            WorkoutCategoryItem(
                name = "Balance & Stability",
                description = "Proprioception, vestibular alignment, ankle stabilizers & joint poise",
                exerciseCount = 5,
                durationMinutes = 25,
                iconName = "balance",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("ba1", "Single-Leg Flamingo Balance", "Balance", "Ankle Invertors/Evertors, Glute Medius", 3, "45 sec/leg", 30, "Beginner", "Stand on one barefoot, opposite knee lifted to hip height, arms outstretched.", "Fix gaze on stationary spot on wall; micro-adjust through foot tripod."),
                    Exercise("ba2", "Arabesque Scale Balance", "Balance", "Posterior Stabilizers, Core", 3, "30 sec hold/side", 45, "Intermediate", "Stand on one leg, hinge forward extending rear leg and arms horizontal.", "Keep hips level with ground and core deeply braced."),
                    Exercise("ba3", "Single-Leg Cone / Point Reaches", "Balance", "Ankle Proprioception, Vastus Medialis", 3, "8 reaches/direction", 45, "Intermediate", "On one leg, reach opposite foot forward, sideways, and backward touching points.", "Keep knee of standing leg aligned over second toe."),
                    Exercise("ba4", "Pistol Squat Balance Assist", "Balance", "Single-Leg Quadriceps, Balance", 3, "6 reps/leg", 60, "Advanced", "Lower into full single-leg squat with opposite leg extended straight forward.", "Maintain heel contact with floor throughout entire descent and ascent."),
                    Exercise("ba5", "Y-Balance Dynamic Drill", "Balance", "Neuromuscular Control, Hip Stabilizers", 3, "6 reps/leg", 45, "Intermediate", "Balance on one foot, reach free foot in Anterior, Posteromedial, Posterolateral directions.", "Do not transfer weight onto reaching foot; tap gently and return.")
                )
            ),
            WorkoutCategoryItem(
                name = "Plyometrics",
                description = "Elastic stretch-shortening cycle, explosive power & tendon spring stiffness",
                exerciseCount = 5,
                durationMinutes = 35,
                iconName = "plyo",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("pl1", "Explosive Box Jumps", "Plyometrics", "Fast-Twitch Hip Extensors, Quads", 4, "6 reps", 90, "Intermediate", "Quarter squat, swing arms, explode onto box landing softly in athletic squat.", "Step down off box rather than jumping down to protect Achilles tendon."),
                    Exercise("pl2", "Broad Jump for Distance", "Plyometrics", "Horizontal Hip Power, Glute Max", 4, "5 reps", 90, "Intermediate", "Two-foot takeoff, explode forward horizontally, absorb landing in deep squat.", "Focus on maximum horizontal displacement and silent landing."),
                    Exercise("pl3", "Lateral Skater Bounds", "Plyometrics", "Lateral Power, Adductors, Glute Medius", 3, "10 bounds/side", 60, "Intermediate", "Bound sideways from one foot to other, absorbing force and immediately exploding back.", "Stick each landing for 1 second before driving into next lateral bound."),
                    Exercise("pl4", "Depth Drops (Shock Absorption)", "Plyometrics", "Eccentric Tendon Deceleration", 3, "6 reps", 90, "Advanced", "Step off 12-inch box, hit floor and instantly decelerate with perfect quiet posture.", "Do not allow knees to collapse inward (valgus) on ground contact."),
                    Exercise("pl5", "Clapping Plyo Pushups", "Plyometrics", "Upper Body Rate of Force Development", 3, "8 reps", 75, "Advanced", "Explosive pushup off floor with enough clearance to clap hands before landing.", "Land with soft elbows to absorb impact smoothly into the next repetition.")
                )
            ),
            WorkoutCategoryItem(
                name = "Kettlebell Training",
                description = "Offset mass dynamics, pendulum momentum & explosive ballistic conditioning",
                exerciseCount = 5,
                durationMinutes = 35,
                iconName = "kettlebell",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("kb1", "Kettlebell Swings (Hardstyle)", "Kettlebell", "Glutes, Hamstrings, Core, Grip", 4, "15 reps", 60, "Intermediate", "Hike kettlebell high between legs, snap hips forward with explosive glute contraction.", "The swing is a hip hinge, not a squat; keep arms like loose ropes."),
                    Exercise("kb2", "Kettlebell Goblet Squat", "Kettlebell", "Quads, Core, Hip Mobility", 4, "12 reps", 60, "Beginner", "Hold kettlebell by horns at chest, sink into deep squat with elbows inside knees.", "Use elbows to gently push knees outward at the bottom of the squat."),
                    Exercise("kb3", "Kettlebell Windmills", "Kettlebell", "Obliques, Hamstring Mobility, Scapular Stability", 3, "8 reps/side", 60, "Advanced", "Press kettlebell overhead, turn feet 45 degrees, hinge down to touch floor.", "Keep eyes on kettlebell overhead throughout the entire descent."),
                    Exercise("kb4", "Kettlebell Clean & Press", "Kettlebell", "Whole Body Pull & Overhead Drive", 4, "8 reps/arm", 60, "Intermediate", "Clean kettlebell to rack position smoothly, press vertically with tight core.", "Tuck elbow into ribs in rack position; do not let bell bang forearm."),
                    Exercise("kb5", "Kettlebell Halo", "Kettlebell", "Shoulder Capsule, Cervicothoracic Core", 3, "10 circles/dir", 45, "Beginner", "Hold kettlebell upside down by horns, circle around head smoothly.", "Keep torso and head completely stationary; move only through arms.")
                )
            ),
            WorkoutCategoryItem(
                name = "Resistance Band",
                description = "Accommodating resistance, joint-friendly peak tension & pump volume",
                exerciseCount = 5,
                durationMinutes = 30,
                iconName = "band",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("rb1", "Banded Pull-Aparts", "Resistance Band", "Rear Deltoids, Rhomboids", 4, "20 reps", 30, "Beginner", "Hold band shoulder-width with straight arms, pull hands wide touching chest.", "Keep shoulders depressed; squeeze shoulder blades together at apex."),
                    Exercise("rb2", "Banded Face Pulls with External Rotation", "Resistance Band", "Rotator Cuff, Upper Traps, Infraspinatus", 4, "15 reps", 45, "Beginner", "Anchor band high, pull to forehead level pulling knuckles backward.", "Critical exercise for reversing rounded shoulders and bench press imbalance."),
                    Exercise("rb3", "Banded Good Mornings", "Resistance Band", "Hamstrings, Glutes, Erector Spinae", 3, "15 reps", 45, "Beginner", "Loop band under feet and around neck, hinge backward with flat back.", "Feel deep stretch in hamstrings; drive hips forward into band tension."),
                    Exercise("rb4", "Banded Pallof Press", "Resistance Band", "Anti-Rotation Core, Transverse Abdominis", 3, "12 reps/side (3s hold)", 45, "Intermediate", "Anchor band at chest height, stand sideways, press arms straight out and resist rotation.", "Do not allow band to pull hands toward anchor; stay rock-solid."),
                    Exercise("rb5", "Banded Bicep / Tricep Superset", "Resistance Band", "Arms Hypertrophy", 3, "15 reps each", 45, "Beginner", "Stand on band for curls, then loop overhead for tricep pushdowns.", "Keep constant tension; avoid letting band snap back slack.")
                )
            ),
            WorkoutCategoryItem(
                name = "Dumbbell Training",
                description = "Unilateral balance, muscular symmetry & isolated free-weight hypertrophy",
                exerciseCount = 5,
                durationMinutes = 40,
                iconName = "dumbbell",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("db1", "Dumbbell Arnold Press", "Dumbbell", "All 3 Deltoid Heads", 4, "10-12 reps", 60, "Intermediate", "Start palms facing face at chin, rotate outward as you press overhead.", "Smooth rotation throughout movement; avoid locking out elbows aggressively."),
                    Exercise("db2", "Dumbbell Romanian Deadlifts", "Dumbbell", "Hamstrings, Glutes, Erector Spinae", 4, "10-12 reps", 60, "Intermediate", "Hold dumbbells in front of thighs, hinge hips back keeping weights tight to legs.", "Stop descent when hips stop moving backward; keep lats latched."),
                    Exercise("db3", "Dumbbell Incline Chest Flyes", "Dumbbell", "Pectoral Clavicular, Stretch Hypertrophy", 3, "12 reps", 60, "Intermediate", "Slight elbow bend, arc dumbbells outward until deep chest stretch, hug back up.", "Do not over-stretch shoulders beyond chest plane at bottom."),
                    Exercise("db4", "Dumbbell Pullovers across Bench", "Dumbbell", "Serratus Anterior, Lats, Ribcage Expansion", 3, "12 reps", 60, "Intermediate", "Upper back across bench, lower dumbbell behind head in wide arc.", "Keep hips lower than bench to maximize stretch across ribcage."),
                    Exercise("db5", "Dumbbell Thrusters", "Dumbbell", "Quads, Glutes, Deltoids, Cardiovascular Engine", 4, "10 reps", 75, "Advanced", "Front rack dumbbells, deep squat, use ascending leg momentum to press overhead.", "Breathe out explosively as you stand and press into full lockout.")
                )
            ),
            WorkoutCategoryItem(
                name = "Bodyweight Skills",
                description = "Relative strength mastery, gymnastic levers & advanced calisthenics",
                exerciseCount = 5,
                durationMinutes = 35,
                iconName = "calisthenics",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("bw1", "Handstand Wall Holds", "Bodyweight Skills", "Shoulder Girdle, Wrist Flexors, Core", 4, "30-45 sec hold", 60, "Intermediate", "Kick up against wall, push floor away actively with straight arms.", "Pull ribs in and squeeze glutes; avoid excessive banana back arching."),
                    Exercise("bw2", "L-Sit Hold on Floor / Dip Bars", "Bodyweight Skills", "Hip Flexors, Rectus Abdominis, Triceps", 4, "15-20 sec hold", 60, "Advanced", "Press palms into floor, elevate hips and legs parallel to floor with straight knees.", "Point toes and depress shoulder blades with maximal downward press."),
                    Exercise("bw3", "Archer Pushups", "Bodyweight Skills", "Single-Arm Chest Power, Triceps", 3, "8 reps/side", 60, "Advanced", "Wide stance pushup, lower toward one hand while extending other arm straight out.", "Allows progressive overload toward the one-arm pushup."),
                    Exercise("bw4", "Dragon Flag Progression", "Bodyweight Skills", "Entire Anterior Kinetic Chain", 3, "6-8 slow reps", 75, "Advanced", "Grip bench behind head, raise straight body from shoulders with core braced.", "Do not bend at the hips; keep body completely straight as a flagpole."),
                    Exercise("bw5", "Muscle-Up Transition Bar Dips", "Bodyweight Skills", "Upper Chest, Lats, Explosive Pull", 4, "8 reps", 60, "Advanced", "Deep dip on single straight bar, leaning torso forward over bar.", "Control the bottom depth to protect anterior shoulder capsule.")
                )
            ),
            WorkoutCategoryItem(
                name = "Recovery & Mobility",
                description = "Tissue rehydration, parasympathetic downregulation & lactic acid clearance",
                exerciseCount = 5,
                durationMinutes = 25,
                iconName = "recovery",
                isSpecialtyCategory = true,
                exercises = listOf(
                    Exercise("rc1", "Foam Rolling IT Band & Quads", "Recovery", "Tensor Fasciae Latae, Rectus Femoris", 3, "60 sec/side", 30, "Beginner", "Roll slowly along outer thigh, pause on trigger points for 20 seconds.", "Breathe steadily; do not tense up or roll directly over knee joint."),
                    Exercise("rc2", "Child's Pose with Lateral Lat Reach", "Recovery", "Lats, Lower Back, Parasympathetic System", 3, "45 sec hold/side", 30, "Beginner", "Kneel, sink hips to heels, walk hands diagonally to left, then right.", "Feel the deep side-body stretch from hip to armpit."),
                    Exercise("rc3", "Legs-Up-The-Wall Venous Drainage", "Recovery", "Venous Return, Lymphatic Drainage, Hamstrings", 1, "5 minutes", 30, "Beginner", "Lie on back with hips close to wall, legs extended vertically upward.", "Accelerates blood return from lower extremities and calms heart rate."),
                    Exercise("rc4", "Pigeon Pose Glute Release", "Recovery", "Piriformis, Deep Gluteal Muscles", 3, "60 sec/side", 30, "Intermediate", "Bring front shin parallel to front of mat, extend rear leg straight back.", "Lower torso onto elbows or mat for deeper posterior hip release."),
                    Exercise("rc5", "Diaphragmatic Box Breathing Stretch", "Recovery", "Vagus Nerve, Diaphragm, Core Decompression", 1, "4 minutes", 0, "Beginner", "Inhale 4s, hold 4s, exhale 4s, hold 4s while lying flat on back with hand on belly.", "Lowers systemic cortisol, signaling body to begin muscle repair.")
                )
            )
        )
    }

    fun getAllWorkoutCategories(): List<WorkoutCategoryItem> {
        return getWorkoutCategories() + getSpecialtyWorkoutCategories()
    }

    fun getTodayMealPlan(profile: UserProfile?): List<MealItem> {
        val totalCal = profile?.dailyCalorieTarget ?: 2500
        val isBulking = totalCal >= 2400

        return listOf(
            MealItem(
                id = "m1",
                mealType = "Breakfast",
                name = "Oats & Whey Power Bowl with Almonds",
                hindiTitle = "ओट्स और वे प्रोटीन बाउल",
                calories = (totalCal * 0.28).roundToInt(),
                protein = if (isBulking) 38 else 32,
                carbs = if (isBulking) 65 else 45,
                fats = 14,
                description = "Rolled oats cooked with skim milk/water, 1 scoop whey protein isolate, sliced banana, chia seeds & crushed almonds.",
                isVeg = true
            ),
            MealItem(
                id = "m2",
                mealType = "Lunch",
                name = "High-Protein Paneer Bhurji / Soya with Roti & Dal",
                hindiTitle = "पनीर भुर्जी, 2 मल्टीग्रेन रोटी और पीली दाल",
                calories = (totalCal * 0.35).roundToInt(),
                protein = if (isBulking) 46 else 40,
                carbs = if (isBulking) 80 else 60,
                fats = 18,
                description = "Fresh crushed low-fat paneer or sautéed soya chunks with onions, tomatoes, turmeric, paired with 2 multigrain rotis, yellow dal and cucumber salad.",
                isVeg = true
            ),
            MealItem(
                id = "m3",
                mealType = "Evening Snack",
                name = "Boiled Sprouts Chaat & Roasted Chana",
                hindiTitle = "अंकुरित मूंग चाट और भुना चना",
                calories = (totalCal * 0.15).roundToInt(),
                protein = 18,
                carbs = 35,
                fats = 6,
                description = "Steamed moong sprouts tossed with lemon juice, tomatoes, green chilies, chaat masala and a handful of roasted chana.",
                isVeg = true
            ),
            MealItem(
                id = "m4",
                mealType = "Dinner",
                name = "Grilled Protein Plate with Quinoa / Brown Rice & Steamed Veggies",
                hindiTitle = "ग्रिल्ड प्रोटीन थाली और ब्राउन राइस",
                calories = (totalCal * 0.22).roundToInt(),
                protein = if (isBulking) 42 else 38,
                carbs = if (isBulking) 55 else 35,
                fats = 12,
                description = "Grilled herbed tofu/paneer with steamed broccoli, carrots, bell peppers and 1 cup of light brown jeera rice.",
                isVeg = true
            )
        )
    }
}

