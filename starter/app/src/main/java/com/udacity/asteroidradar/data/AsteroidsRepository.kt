package com.udacity.asteroidradar.data

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class AsteroidsRepository(private val asteroidsDatabase: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> = asteroidsDatabase.dao.getAsteroids(getTodayDate())

    val imageOfTheDay: LiveData<PictureOfDay> = asteroidsDatabase.dao.getPictureOfDay()


    suspend fun refreshCachedAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val inputStream =
                    Network.nasaApi.getAsteroids(startDate, endDate).byteStream()
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuffer = StringBuffer()
                bufferedReader.forEachLine {
                    stringBuffer.append("$it\n")
                }
                val playlist = parseAsteroidsJsonResult(JSONObject(stringBuffer.toString()))
                asteroidsDatabase.dao.insertAsteroids(*playlist.toTypedArray())
            }
        }
    }

    suspend fun deleteRecordsBeforeToday(today: String) {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                asteroidsDatabase.dao.deletePreviousDays(today)
            }
        }
    }

    suspend fun getImageOfTheDay() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Network.nasaApi.getImageOfTheDay().let {
                    if (it.mediaType == "image") {
                        asteroidsDatabase.dao.insertPictureOfDay(Network.nasaApi.getImageOfTheDay())
                    }
                }
            }
        }
    }

}