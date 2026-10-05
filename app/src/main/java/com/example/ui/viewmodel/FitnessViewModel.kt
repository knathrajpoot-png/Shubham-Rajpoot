package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FitnessDatabase
import com.example.data.model.DailyActivity
import com.example.data.model.Exercise
import com.example.data.model.MealItem
import com.example.data.model.ReminderItem
import com.example.data.model.RunSession
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutCategoryItem
import com.example.data.repository.FitnessRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

sealed class AppScreen {
    object Splash : AppScreen()
    object Login : AppScreen()
    object ProfileSetup : AppScreen()
    object MainDashboard : AppScreen() // Bottom nav container
    data class WorkoutDetail(val category: WorkoutCategoryItem) : AppScreen()
    data class ActiveWorkoutSession(val categoryName: String, val exercises: List<Exercise>) : AppScreen()
    object RunningTracker : AppScreen()
    object MeditationPlayer : AppScreen()
    object RemindersList : AppScreen()
}

enum class BottomTab {
    HOME, WORKOUT, DIET, ACTIVITY, PROFILE
}

class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FitnessDatabase.getDatabase(application)
    val repository = FitnessRepository(db)

    // Current Screen
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Splash)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Current Bottom Tab
    private val _currentTab = MutableStateFlow(BottomTab.HOME)
    val currentTab: StateFlow<BottomTab> = _currentTab.asStateFlow()

    // Screen back stack
    private val screenStack = mutableListOf<AppScreen>()

    // Room Flows
    val userProfile: StateFlow<UserProfile?> = repository.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val todayActivity: StateFlow<DailyActivity?> = repository.getTodayActivity().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val allRuns: StateFlow<List<RunSession>> = repository.allRuns.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allReminders: StateFlow<List<ReminderItem>> = repository.allReminders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Meals state
    private val _todayMeals = MutableStateFlow<List<MealItem>>(emptyList())
    val todayMeals: StateFlow<List<MealItem>> = _todayMeals.asStateFlow()

    // Login & OTP state
    private val _mobileNumber = MutableStateFlow("9876543210")
    val mobileNumber: StateFlow<String> = _mobileNumber.asStateFlow()

    private val _countryCode = MutableStateFlow("+91")
    val countryCode: StateFlow<String> = _countryCode.asStateFlow()

    private val _isOtpSent = MutableStateFlow(false)
    val isOtpSent: StateFlow<Boolean> = _isOtpSent.asStateFlow()

    private val _enteredOtp = MutableStateFlow("")
    val enteredOtp: StateFlow<String> = _enteredOtp.asStateFlow()

    private val _otpCountdown = MutableStateFlow(30)
    val otpCountdown: StateFlow<Int> = _otpCountdown.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private var otpTimerJob: Job? = null

    // Onboarding Setup State
    private val _setupStep = MutableStateFlow(1) // 1..4
    val setupStep: StateFlow<Int> = _setupStep.asStateFlow()

    private val _setupName = MutableStateFlow("Shubham Rajpoot")
    val setupName: StateFlow<String> = _setupName.asStateFlow()

    private val _setupAge = MutableStateFlow("25")
    val setupAge: StateFlow<String> = _setupAge.asStateFlow()

    private val _setupWeight = MutableStateFlow("75")
    val setupWeight: StateFlow<String> = _setupWeight.asStateFlow()

    private val _setupHeight = MutableStateFlow("178")
    val setupHeight: StateFlow<String> = _setupHeight.asStateFlow()

    private val _setupGender = MutableStateFlow("Male")
    val setupGender: StateFlow<String> = _setupGender.asStateFlow()

    private val _setupGoal = MutableStateFlow("Muscle Gain")
    val setupGoal: StateFlow<String> = _setupGoal.asStateFlow()

    private val _isGeneratingPlan = MutableStateFlow(false)
    val isGeneratingPlan: StateFlow<Boolean> = _isGeneratingPlan.asStateFlow()

    private val _planGenerationStep = MutableStateFlow("Analyzing your profile...")
    val planGenerationStep: StateFlow<String> = _planGenerationStep.asStateFlow()

    // Running Session State
    private val _isRunningActive = MutableStateFlow(false)
    val isRunningActive: StateFlow<Boolean> = _isRunningActive.asStateFlow()

    private val _runDurationSeconds = MutableStateFlow(0L)
    val runDurationSeconds: StateFlow<Long> = _runDurationSeconds.asStateFlow()

    private val _runDistanceKm = MutableStateFlow(0.0f)
    val runDistanceKm: StateFlow<Float> = _runDistanceKm.asStateFlow()

    private val _runCalories = MutableStateFlow(0)
    val runCalories: StateFlow<Int> = _runCalories.asStateFlow()

    private val _runPace = MutableStateFlow("0'00\"")
    val runPace: StateFlow<String> = _runPace.asStateFlow()

    private var runTimerJob: Job? = null

    // Active Workout Player State
    private val _activeExerciseIndex = MutableStateFlow(0)
    val activeExerciseIndex: StateFlow<Int> = _activeExerciseIndex.asStateFlow()

    private val _activeCurrentSet = MutableStateFlow(1)
    val activeCurrentSet: StateFlow<Int> = _activeCurrentSet.asStateFlow()

    private val _isResting = MutableStateFlow(false)
    val isResting: StateFlow<Boolean> = _isResting.asStateFlow()

    private val _restSecondsRemaining = MutableStateFlow(45)
    val restSecondsRemaining: StateFlow<Int> = _restSecondsRemaining.asStateFlow()

    private var restTimerJob: Job? = null

    // Meditation State
    private val _meditationDurationMinutes = MutableStateFlow(10)
    val meditationDurationMinutes: StateFlow<Int> = _meditationDurationMinutes.asStateFlow()

    private val _meditationSecondsRemaining = MutableStateFlow(600)
    val meditationSecondsRemaining: StateFlow<Int> = _meditationSecondsRemaining.asStateFlow()

    private val _isMeditationRunning = MutableStateFlow(false)
    val isMeditationRunning: StateFlow<Boolean> = _isMeditationRunning.asStateFlow()

    private val _breathingPhase = MutableStateFlow("Inhale")
    val breathingPhase: StateFlow<String> = _breathingPhase.asStateFlow()

    private var meditationTimerJob: Job? = null

    // Notifications Dialog
    private val _showNotificationsDialog = MutableStateFlow(false)
    val showNotificationsDialog: StateFlow<Boolean> = _showNotificationsDialog.asStateFlow()

    // Menu Drawer
    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureInitialData()
            // Observe profile to populate meals
            userProfile.collect { profile ->
                if (profile != null) {
                    _todayMeals.value = repository.getTodayMealPlan(profile)
                }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        screenStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
            return true
        }
        return false
    }

    fun setBottomTab(tab: BottomTab) {
        _currentTab.value = tab
        if (_currentScreen.value !is AppScreen.MainDashboard) {
            _currentScreen.value = AppScreen.MainDashboard
        }
    }

    fun toggleDrawer(open: Boolean) {
        _isDrawerOpen.value = open
    }

    fun toggleNotifications(show: Boolean) {
        _showNotificationsDialog.value = show
    }

    // Auth & OTP Handlers
    fun updateMobileNumber(number: String) {
        _mobileNumber.value = number.filter { it.isDigit() }.take(10)
        _loginError.value = null
    }

    fun sendOtp() {
        if (_mobileNumber.value.length < 10) {
            _loginError.value = "Please enter a valid 10-digit mobile number"
            return
        }
        _isOtpSent.value = true
        _loginError.value = null
        startOtpCountdown()
    }

    private fun startOtpCountdown() {
        otpTimerJob?.cancel()
        _otpCountdown.value = 30
        otpTimerJob = viewModelScope.launch {
            while (_otpCountdown.value > 0) {
                delay(1000)
                _otpCountdown.value -= 1
            }
        }
    }

    fun updateEnteredOtp(otp: String) {
        _enteredOtp.value = otp.filter { it.isDigit() }.take(6)
        _loginError.value = null
    }

    fun quickFillDemoOtp() {
        _enteredOtp.value = "123456"
        _loginError.value = null
    }

    fun verifyOtp() {
        if (_enteredOtp.value.length != 6) {
            _loginError.value = "Please enter the complete 6-digit OTP"
            return
        }
        // Accept any 6-digit OTP for seamless verification
        viewModelScope.launch {
            val profile = db.userProfileDao().getUserProfileSync()
            if (profile != null && profile.isOnboardingCompleted) {
                _currentScreen.value = AppScreen.MainDashboard
            } else {
                _currentScreen.value = AppScreen.ProfileSetup
            }
        }
    }

    // Onboarding Handlers
    fun updateSetupName(name: String) { _setupName.value = name }
    fun updateSetupAge(age: String) { _setupAge.value = age.filter { it.isDigit() }.take(3) }
    fun updateSetupWeight(weight: String) { _setupWeight.value = weight.filter { it.isDigit() || it == '.' }.take(5) }
    fun updateSetupHeight(height: String) { _setupHeight.value = height.filter { it.isDigit() || it == '.' }.take(5) }
    fun updateSetupGender(gender: String) { _setupGender.value = gender }
    fun updateSetupGoal(goal: String) { _setupGoal.value = goal }

    fun nextSetupStep() {
        if (_setupStep.value < 3) {
            _setupStep.value += 1
        } else if (_setupStep.value == 3) {
            createPersonalizedPlan()
        }
    }

    fun prevSetupStep() {
        if (_setupStep.value > 1) {
            _setupStep.value -= 1
        }
    }

    private fun createPersonalizedPlan() {
        _setupStep.value = 4
        _isGeneratingPlan.value = true
        viewModelScope.launch {
            _planGenerationStep.value = "Analyzing your profile..."
            delay(800)
            _planGenerationStep.value = "Calculating optimal caloric & macronutrient targets..."
            delay(900)
            _planGenerationStep.value = "Preparing customized workout splits & exercises..."
            delay(900)
            _planGenerationStep.value = "Preparing your nutrition & hydration plan..."
            delay(800)

            val ageInt = _setupAge.value.toIntOrNull() ?: 25
            val weightF = _setupWeight.value.toFloatOrNull() ?: 75f
            val heightF = _setupHeight.value.toFloatOrNull() ?: 178f

            val profile = repository.saveProfileAndGeneratePlan(
                name = _setupName.value,
                age = ageInt,
                weightKg = weightF,
                heightCm = heightF,
                gender = _setupGender.value,
                goal = _setupGoal.value,
                mobileNumber = "${_countryCode.value} ${_mobileNumber.value}"
            )
            _todayMeals.value = repository.getTodayMealPlan(profile)
            _isGeneratingPlan.value = false
            _currentScreen.value = AppScreen.MainDashboard
        }
    }

    // Quick Actions
    fun addQuickWater() {
        vibrate(50)
        viewModelScope.launch {
            repository.addWaterQuick(0.25f)
        }
    }

    fun addQuickSteps() {
        vibrate(50)
        viewModelScope.launch {
            repository.addStepsQuick(500)
        }
    }

    fun toggleMealItem(mealId: String) {
        vibrate(40)
        _todayMeals.value = _todayMeals.value.map {
            if (it.id == mealId) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    // Active Workout Player
    fun startWorkoutSession(categoryName: String, exercises: List<Exercise>) {
        _activeExerciseIndex.value = 0
        _activeCurrentSet.value = 1
        _isResting.value = false
        _currentScreen.value = AppScreen.ActiveWorkoutSession(categoryName, exercises)
    }

    fun completeSet(totalSets: Int, restSecs: Int) {
        vibrate(60)
        if (_activeCurrentSet.value < totalSets) {
            _activeCurrentSet.value += 1
            startRestTimer(restSecs)
        } else {
            // Next Exercise
            nextExercise(totalSets)
        }
    }

    fun nextExercise(totalSets: Int) {
        val session = _currentScreen.value as? AppScreen.ActiveWorkoutSession ?: return
        if (_activeExerciseIndex.value < session.exercises.size - 1) {
            _activeExerciseIndex.value += 1
            _activeCurrentSet.value = 1
            startRestTimer(45)
        } else {
            // Finish workout
            finishWorkout(session.categoryName, session.exercises.size * 5)
        }
    }

    fun prevExercise() {
        if (_activeExerciseIndex.value > 0) {
            _activeExerciseIndex.value -= 1
            _activeCurrentSet.value = 1
            _isResting.value = false
            restTimerJob?.cancel()
        }
    }

    private fun startRestTimer(seconds: Int) {
        _isResting.value = true
        _restSecondsRemaining.value = seconds
        restTimerJob?.cancel()
        restTimerJob = viewModelScope.launch {
            while (_restSecondsRemaining.value > 0) {
                delay(1000)
                _restSecondsRemaining.value -= 1
            }
            _isResting.value = false
            vibrate(120)
        }
    }

    fun skipRest() {
        restTimerJob?.cancel()
        _isResting.value = false
    }

    fun finishWorkout(categoryName: String, durationMin: Int = 35) {
        restTimerJob?.cancel()
        _isResting.value = false
        vibrate(200)
        viewModelScope.launch {
            val calories = durationMin * 8
            repository.logCompletedWorkout(
                title = "$categoryName Power Workout",
                category = categoryName,
                durationMinutes = durationMin,
                calories = calories
            )
            _currentScreen.value = AppScreen.MainDashboard
            _currentTab.value = BottomTab.ACTIVITY
        }
    }

    // Running Tracker
    fun toggleRunning() {
        vibrate(80)
        if (_isRunningActive.value) {
            // Pause
            _isRunningActive.value = false
            runTimerJob?.cancel()
        } else {
            // Start or Resume
            _isRunningActive.value = true
            runTimerJob = viewModelScope.launch {
                while (_isRunningActive.value) {
                    delay(1000)
                    _runDurationSeconds.value += 1
                    // Simulated realistic distance increment: ~10 km/h = ~2.77 m/s
                    _runDistanceKm.value += 0.0028f
                    _runCalories.value = (_runDistanceKm.value * 68).roundToInt()

                    if (_runDistanceKm.value > 0.05f) {
                        val paceSeconds = (_runDurationSeconds.value / _runDistanceKm.value).toLong()
                        val paceMin = paceSeconds / 60
                        val paceSec = paceSeconds % 60
                        _runPace.value = "$paceMin'${String.format("%02d", paceSec)}\""
                    }
                }
            }
        }
    }

    fun finishRun() {
        vibrate(150)
        _isRunningActive.value = false
        runTimerJob?.cancel()
        val dist = _runDistanceKm.value
        val duration = _runDurationSeconds.value
        val cals = _runCalories.value
        val paceVal = if (dist > 0.05f) (duration.toFloat() / 60f) / dist else 5.5f

        viewModelScope.launch {
            if (dist > 0.05f) {
                repository.saveRunSession(dist, duration, cals, paceVal)
            }
            // Reset
            _runDurationSeconds.value = 0L
            _runDistanceKm.value = 0.0f
            _runCalories.value = 0
            _runPace.value = "0'00\""
            _currentScreen.value = AppScreen.MainDashboard
            _currentTab.value = BottomTab.ACTIVITY
        }
    }

    // Meditation Handlers
    fun selectMeditationDuration(minutes: Int) {
        _meditationDurationMinutes.value = minutes
        _meditationSecondsRemaining.value = minutes * 60
        _isMeditationRunning.value = false
        meditationTimerJob?.cancel()
    }

    fun toggleMeditation() {
        vibrate(80)
        if (_isMeditationRunning.value) {
            _isMeditationRunning.value = false
            meditationTimerJob?.cancel()
        } else {
            _isMeditationRunning.value = true
            meditationTimerJob = viewModelScope.launch {
                var cycleCounter = 0
                while (_isMeditationRunning.value && _meditationSecondsRemaining.value > 0) {
                    delay(1000)
                    _meditationSecondsRemaining.value -= 1
                    cycleCounter = (cycleCounter + 1) % 16
                    _breathingPhase.value = when (cycleCounter) {
                        in 0..3 -> "Inhale"
                        in 4..7 -> "Hold"
                        in 8..11 -> "Exhale"
                        else -> "Rest"
                    }
                }
                if (_meditationSecondsRemaining.value <= 0) {
                    _isMeditationRunning.value = false
                    vibrate(300)
                }
            }
        }
    }

    // Reminders
    fun toggleReminder(reminder: ReminderItem) {
        vibrate(40)
        viewModelScope.launch {
            repository.toggleReminder(reminder)
        }
    }

    fun addNewReminder(type: String, title: String, time: String) {
        viewModelScope.launch {
            repository.addReminder(
                ReminderItem(
                    type = type,
                    title = title,
                    timeString = time,
                    isEnabled = true
                )
            )
        }
    }

    // Profile & Goal Updates
    fun updateGoal(newGoal: String) {
        vibrate(50)
        val current = userProfile.value ?: return
        viewModelScope.launch {
            repository.saveProfileAndGeneratePlan(
                name = current.name,
                age = current.age,
                weightKg = current.weightKg,
                heightCm = current.heightCm,
                gender = current.gender,
                goal = newGoal,
                mobileNumber = current.mobileNumber
            )
        }
    }

    fun updateMeasurements(weightKg: Float, heightCm: Float) {
        val current = userProfile.value ?: return
        viewModelScope.launch {
            repository.saveProfileAndGeneratePlan(
                name = current.name,
                age = current.age,
                weightKg = weightKg,
                heightCm = heightCm,
                gender = current.gender,
                goal = current.goal,
                mobileNumber = current.mobileNumber
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            db.userProfileDao().updateOnboardingStatus(false)
            _currentScreen.value = AppScreen.Login
            _isOtpSent.value = false
            _enteredOtp.value = ""
        }
    }

    private fun vibrate(durationMs: Long) {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } catch (_: Exception) { }
    }
}
