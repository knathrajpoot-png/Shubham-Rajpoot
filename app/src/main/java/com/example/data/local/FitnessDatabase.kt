package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.data.model.CompletedWorkout
import com.example.data.model.DailyActivity
import com.example.data.model.ReminderItem
import com.example.data.model.RunSession
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileSync(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfile)

    @Query("UPDATE user_profile SET isOnboardingCompleted = :completed WHERE id = 1")
    suspend fun updateOnboardingStatus(completed: Boolean)
}

@Dao
interface DailyActivityDao {
    @Query("SELECT * FROM daily_activity WHERE dateString = :date LIMIT 1")
    fun getActivityForDate(date: String): Flow<DailyActivity?>

    @Query("SELECT * FROM daily_activity WHERE dateString = :date LIMIT 1")
    suspend fun getActivityForDateSync(date: String): DailyActivity?

    @Query("SELECT * FROM daily_activity ORDER BY dateString DESC LIMIT 30")
    fun getRecentActivities(): Flow<List<DailyActivity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(activity: DailyActivity)

    @Query("UPDATE daily_activity SET waterIntakeLiters = waterIntakeLiters + :liters WHERE dateString = :date")
    suspend fun addWater(date: String, liters: Float)

    @Query("UPDATE daily_activity SET steps = steps + :stepDelta, caloriesBurned = caloriesBurned + :calorieDelta WHERE dateString = :date")
    suspend fun addSteps(date: String, stepDelta: Int, calorieDelta: Int)
}

@Dao
interface RunSessionDao {
    @Query("SELECT * FROM run_session ORDER BY timestamp DESC")
    fun getAllRuns(): Flow<List<RunSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: RunSession)
}

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders ORDER BY id ASC")
    fun getAllReminders(): Flow<List<ReminderItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderItem)

    @Update
    suspend fun updateReminder(reminder: ReminderItem)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminder(id: Long)
}

@Dao
interface CompletedWorkoutDao {
    @Query("SELECT * FROM completed_workouts ORDER BY timestamp DESC")
    fun getAllWorkouts(): Flow<List<CompletedWorkout>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: CompletedWorkout)
}

@Database(
    entities = [
        UserProfile::class,
        DailyActivity::class,
        RunSession::class,
        ReminderItem::class,
        CompletedWorkout::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitnessDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun dailyActivityDao(): DailyActivityDao
    abstract fun runSessionDao(): RunSessionDao
    abstract fun reminderDao(): ReminderDao
    abstract fun completedWorkoutDao(): CompletedWorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: FitnessDatabase? = null

        fun getDatabase(context: Context): FitnessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabase::class.java,
                    "thefit_shubham_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
