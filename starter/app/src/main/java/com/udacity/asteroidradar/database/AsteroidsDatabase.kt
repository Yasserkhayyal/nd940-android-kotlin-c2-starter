package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidsDao {
    @Query("select * from Asteroid WHERE closeApproachDate >= :startDate ORDER BY closeApproachDate ASC")
    fun getAsteroids(startDate: String): LiveData<List<Asteroid>>

    @Query("select * from PictureOfDay")
    fun getPictureOfDay(): LiveData<PictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(vararg asteroids: Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(imageOfDay: PictureOfDay)

    @Query("DELETE from Asteroid WHERE closeApproachDate <= :today")
    fun deletePreviousDays(today: String)
}

@Database(entities = [Asteroid::class, PictureOfDay::class], version = 1, exportSchema = false)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val dao: AsteroidsDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            ).fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}



