package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getSeventhDayDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.data.AsteroidsRepository
import com.udacity.asteroidradar.database.getDatabase
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)
        return try {
            repository.refreshCachedAsteroids(getTodayDate(), getSeventhDayDate())
            repository.getImageOfTheDay()
            Result.success()
        } catch (e: HttpException) {
            Result.failure()
        }
    }
}
