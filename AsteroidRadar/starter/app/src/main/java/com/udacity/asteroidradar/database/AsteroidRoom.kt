package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Date

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroid_table")
    fun getAllAsteroids() : LiveData<List<AsteroidEntity>>


    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate >= :dateOfToday")
    fun getAsteroidForWeek(dateOfToday : String = Date.getDateOfToday()) : LiveData<List<AsteroidEntity>>


    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate = :dateOfToday")
    fun getAsteroidForToday(dateOfToday : String = Date.getDateOfToday()) : LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)
}

@Database(entities = [AsteroidEntity::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract val asteroidDao : AsteroidDao

    companion object {

        @Volatile
        private var INSTANCE: AsteroidDatabase? = null

        fun getInstance(context: Context): AsteroidDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
