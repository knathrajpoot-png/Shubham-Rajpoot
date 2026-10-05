package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.WorkoutCategoryItem
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.FitnessNavigationDrawerContent
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.TopAppBarHeader
import com.example.ui.screens.ActiveWorkoutScreen
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.DietScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MeditationScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProfileSetupScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.RunningScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WorkoutScreen
import com.example.ui.theme.FitBlack
import com.example.ui.theme.TheFitShubhamTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.BottomTab
import com.example.ui.viewmodel.FitnessViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheFitShubhamTheme {
                FitnessAppRoot()
            }
        }
    }
}

@Composable
fun FitnessAppRoot(
    viewModel: FitnessViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val dailyActivity by viewModel.todayActivity.collectAsStateWithLifecycle()
    val allRuns by viewModel.allRuns.collectAsStateWithLifecycle()
    val allReminders by viewModel.allReminders.collectAsStateWithLifecycle()
    val todayMeals by viewModel.todayMeals.collectAsStateWithLifecycle()
    val recentWorkouts by viewModel.repository.recentWorkouts.collectAsStateWithLifecycle(emptyList())

    // Login state
    val mobileNumber by viewModel.mobileNumber.collectAsStateWithLifecycle()
    val countryCode by viewModel.countryCode.collectAsStateWithLifecycle()
    val isOtpSent by viewModel.isOtpSent.collectAsStateWithLifecycle()
    val enteredOtp by viewModel.enteredOtp.collectAsStateWithLifecycle()
    val otpCountdown by viewModel.otpCountdown.collectAsStateWithLifecycle()
    val loginError by viewModel.loginError.collectAsStateWithLifecycle()

    // Onboarding setup state
    val setupStep by viewModel.setupStep.collectAsStateWithLifecycle()
    val setupName by viewModel.setupName.collectAsStateWithLifecycle()
    val setupAge by viewModel.setupAge.collectAsStateWithLifecycle()
    val setupWeight by viewModel.setupWeight.collectAsStateWithLifecycle()
    val setupHeight by viewModel.setupHeight.collectAsStateWithLifecycle()
    val setupGender by viewModel.setupGender.collectAsStateWithLifecycle()
    val setupGoal by viewModel.setupGoal.collectAsStateWithLifecycle()
    val isGeneratingPlan by viewModel.isGeneratingPlan.collectAsStateWithLifecycle()
    val planGenerationStep by viewModel.planGenerationStep.collectAsStateWithLifecycle()

    // Running state
    val isRunningActive by viewModel.isRunningActive.collectAsStateWithLifecycle()
    val runDurationSeconds by viewModel.runDurationSeconds.collectAsStateWithLifecycle()
    val runDistanceKm by viewModel.runDistanceKm.collectAsStateWithLifecycle()
    val runCalories by viewModel.runCalories.collectAsStateWithLifecycle()
    val runPace by viewModel.runPace.collectAsStateWithLifecycle()

    // Active workout state
    val activeExerciseIndex by viewModel.activeExerciseIndex.collectAsStateWithLifecycle()
    val activeCurrentSet by viewModel.activeCurrentSet.collectAsStateWithLifecycle()
    val isResting by viewModel.isResting.collectAsStateWithLifecycle()
    val restSecondsRemaining by viewModel.restSecondsRemaining.collectAsStateWithLifecycle()

    // Meditation state
    val meditationDurationMinutes by viewModel.meditationDurationMinutes.collectAsStateWithLifecycle()
    val meditationSecondsRemaining by viewModel.meditationSecondsRemaining.collectAsStateWithLifecycle()
    val isMeditationRunning by viewModel.isMeditationRunning.collectAsStateWithLifecycle()
    val breathingPhase by viewModel.breathingPhase.collectAsStateWithLifecycle()

    // Dialogs & Drawers
    val showNotificationsDialog by viewModel.showNotificationsDialog.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var selectedWorkoutCategory by remember { mutableStateOf<WorkoutCategoryItem?>(null) }

    // Global back handling
    BackHandler(enabled = currentScreen !is AppScreen.Splash) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else if (currentScreen is AppScreen.ActiveWorkoutSession) {
            viewModel.navigateTo(AppScreen.MainDashboard)
        } else if (currentScreen is AppScreen.RunningTracker || currentScreen is AppScreen.MeditationPlayer || currentScreen is AppScreen.RemindersList) {
            viewModel.navigateTo(AppScreen.MainDashboard)
        } else if (currentScreen is AppScreen.MainDashboard) {
            if (currentTab != BottomTab.HOME) {
                viewModel.setBottomTab(BottomTab.HOME)
            }
        } else {
            viewModel.navigateBack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FitBlack)
    ) {
        when (val screen = currentScreen) {
            is AppScreen.Splash -> {
                SplashScreen(
                    onSplashFinished = {
                        if (userProfile != null && userProfile!!.isOnboardingCompleted) {
                            viewModel.navigateTo(AppScreen.MainDashboard)
                        } else {
                            viewModel.navigateTo(AppScreen.Login)
                        }
                    }
                )
            }

            is AppScreen.Login -> {
                LoginScreen(
                    mobileNumber = mobileNumber,
                    countryCode = countryCode,
                    isOtpSent = isOtpSent,
                    enteredOtp = enteredOtp,
                    otpCountdown = otpCountdown,
                    errorMessage = loginError,
                    onMobileChanged = viewModel::updateMobileNumber,
                    onSendOtp = viewModel::sendOtp,
                    onOtpChanged = viewModel::updateEnteredOtp,
                    onVerifyOtp = viewModel::verifyOtp,
                    onQuickDemoOtp = viewModel::quickFillDemoOtp,
                    onBackToMobile = {
                        // Reset OTP state to edit number
                        viewModel.verifyOtp()
                    }
                )
            }

            is AppScreen.ProfileSetup -> {
                ProfileSetupScreen(
                    currentStep = setupStep,
                    name = setupName,
                    age = setupAge,
                    weight = setupWeight,
                    height = setupHeight,
                    gender = setupGender,
                    goal = setupGoal,
                    isGeneratingPlan = isGeneratingPlan,
                    planGenerationStepText = planGenerationStep,
                    onNameChange = viewModel::updateSetupName,
                    onAgeChange = viewModel::updateSetupAge,
                    onWeightChange = viewModel::updateSetupWeight,
                    onHeightChange = viewModel::updateSetupHeight,
                    onGenderChange = viewModel::updateSetupGender,
                    onGoalChange = viewModel::updateSetupGoal,
                    onNextStep = viewModel::nextSetupStep,
                    onPrevStep = viewModel::prevSetupStep
                )
            }

            is AppScreen.MainDashboard -> {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        FitnessNavigationDrawerContent(
                            userProfile = userProfile,
                            currentTab = currentTab,
                            onSelectTab = { tab ->
                                viewModel.setBottomTab(tab)
                            },
                            onOpenRunning = {
                                viewModel.navigateTo(AppScreen.RunningTracker)
                            },
                            onOpenMeditation = {
                                viewModel.navigateTo(AppScreen.MeditationPlayer)
                            },
                            onOpenReminders = {
                                viewModel.navigateTo(AppScreen.RemindersList)
                            },
                            onCloseDrawer = {
                                coroutineScope.launch { drawerState.close() }
                            },
                            onLogout = viewModel::logout
                        )
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = FitBlack,
                        topBar = {
                            TopAppBarHeader(
                                onMenuClick = {
                                    coroutineScope.launch { drawerState.open() }
                                },
                                onNotificationClick = {
                                    viewModel.toggleNotifications(true)
                                },
                                onAvatarClick = {
                                    viewModel.setBottomTab(BottomTab.PROFILE)
                                }
                            )
                        },
                        bottomBar = {
                            AppBottomNavBar(
                                selectedTab = currentTab,
                                onTabSelected = viewModel::setBottomTab
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentTab) {
                                BottomTab.HOME -> {
                                    HomeScreen(
                                        userProfile = userProfile,
                                        dailyActivity = dailyActivity,
                                        workoutCategories = viewModel.repository.getAllWorkoutCategories(),
                                        onCategoryClick = { category ->
                                            selectedWorkoutCategory = category
                                            viewModel.setBottomTab(BottomTab.WORKOUT)
                                        },
                                        onQuickActionClick = { action ->
                                            when (action) {
                                                "WORKOUT" -> {
                                                    selectedWorkoutCategory = null
                                                    viewModel.setBottomTab(BottomTab.WORKOUT)
                                                }
                                                "DIET" -> viewModel.setBottomTab(BottomTab.DIET)
                                                "RUNNING" -> viewModel.navigateTo(AppScreen.RunningTracker)
                                                "MEDITATION" -> viewModel.navigateTo(AppScreen.MeditationPlayer)
                                            }
                                        },
                                        onStartWorkoutClick = {
                                            val fullBody = viewModel.repository.getWorkoutCategories().first { it.name == "Full Body" }
                                            viewModel.startWorkoutSession(fullBody.name, fullBody.exercises)
                                        },
                                        onViewMealPlanClick = {
                                            viewModel.setBottomTab(BottomTab.DIET)
                                        },
                                        onAddQuickWater = viewModel::addQuickWater,
                                        onAddQuickSteps = viewModel::addQuickSteps
                                    )
                                }

                                BottomTab.WORKOUT -> {
                                    WorkoutScreen(
                                        categories = viewModel.repository.getAllWorkoutCategories(),
                                        initialSelectedCategory = selectedWorkoutCategory,
                                        onSelectCategory = { cat ->
                                            selectedWorkoutCategory = cat
                                        },
                                        onStartWorkout = { category ->
                                            viewModel.startWorkoutSession(category.name, category.exercises)
                                        }
                                    )
                                }

                                BottomTab.DIET -> {
                                    DietScreen(
                                        userProfile = userProfile,
                                        meals = todayMeals,
                                        onToggleMeal = viewModel::toggleMealItem,
                                        onAddWater = viewModel::addQuickWater
                                    )
                                }

                                BottomTab.ACTIVITY -> {
                                    ActivityScreen(
                                        userProfile = userProfile,
                                        dailyActivity = dailyActivity,
                                        recentWorkouts = recentWorkouts
                                    )
                                }

                                BottomTab.PROFILE -> {
                                    ProfileScreen(
                                        userProfile = userProfile,
                                        onGoalChange = viewModel::updateGoal,
                                        onMeasurementsChange = viewModel::updateMeasurements,
                                        onOpenReminders = {
                                            viewModel.navigateTo(AppScreen.RemindersList)
                                        },
                                        onLogout = viewModel::logout
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is AppScreen.ActiveWorkoutSession -> {
                ActiveWorkoutScreen(
                    categoryName = screen.categoryName,
                    exercises = screen.exercises,
                    currentExerciseIndex = activeExerciseIndex,
                    currentSet = activeCurrentSet,
                    isResting = isResting,
                    restSecondsRemaining = restSecondsRemaining,
                    onCompleteSet = viewModel::completeSet,
                    onSkipRest = viewModel::skipRest,
                    onNextExercise = { viewModel.nextExercise(screen.exercises[activeExerciseIndex].sets) },
                    onPrevExercise = viewModel::prevExercise,
                    onFinishWorkout = { viewModel.finishWorkout(screen.categoryName) },
                    onExitWorkout = { viewModel.navigateTo(AppScreen.MainDashboard) }
                )
            }

            is AppScreen.RunningTracker -> {
                RunningScreen(
                    isRunningActive = isRunningActive,
                    runDurationSeconds = runDurationSeconds,
                    runDistanceKm = runDistanceKm,
                    runCalories = runCalories,
                    runPace = runPace,
                    pastRuns = allRuns,
                    onToggleRun = viewModel::toggleRunning,
                    onFinishRun = viewModel::finishRun,
                    onBack = { viewModel.navigateTo(AppScreen.MainDashboard) }
                )
            }

            is AppScreen.MeditationPlayer -> {
                MeditationScreen(
                    selectedDurationMinutes = meditationDurationMinutes,
                    secondsRemaining = meditationSecondsRemaining,
                    isMeditationRunning = isMeditationRunning,
                    breathingPhase = breathingPhase,
                    onSelectDuration = viewModel::selectMeditationDuration,
                    onToggleMeditation = viewModel::toggleMeditation,
                    onBack = { viewModel.navigateTo(AppScreen.MainDashboard) }
                )
            }

            is AppScreen.RemindersList -> {
                RemindersScreen(
                    reminders = allReminders,
                    onToggleReminder = viewModel::toggleReminder,
                    onAddReminder = viewModel::addNewReminder,
                    onBack = { viewModel.navigateTo(AppScreen.MainDashboard) }
                )
            }

            else -> {
                // fallback to dashboard
                viewModel.navigateTo(AppScreen.MainDashboard)
            }
        }

        // Notification Modal Dialog
        if (showNotificationsDialog) {
            val suggestions = viewModel.repository.getAiCoachSuggestions(userProfile, dailyActivity)
            NotificationsDialog(
                insights = suggestions,
                onDismiss = { viewModel.toggleNotifications(false) }
            )
        }
    }
}
