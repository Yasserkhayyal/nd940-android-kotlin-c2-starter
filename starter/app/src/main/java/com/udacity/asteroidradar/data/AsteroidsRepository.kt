package com.udacity.asteroidradar.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.getSeventhDayDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.PictureOfDay
import com.udacity.asteroidradar.main.FilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class AsteroidsRepository(private val asteroidsDatabase: AsteroidsDatabase) {

    val asteroids = MediatorLiveData<List<Asteroid>>().apply {
        addSource(this, asteroidsDatabase.dao.getAsteroids(getTodayDate()))
    }

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

    fun getFilteredResults(filterType: FilterType) {
        when (filterType) {
            FilterType.WEEK -> addSource(
                asteroids,
                asteroidsDatabase.dao.getAsteroidsForPeriod(
                    getTodayDate(),
                    getSeventhDayDate()
                )
            )
            FilterType.TODAY -> addSource(
                asteroids,
                asteroidsDatabase.dao.getAsteroidsForDate(getTodayDate())
            )
            else -> addSource(asteroids, asteroidsDatabase.dao.getAsteroids(getTodayDate()))
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

    private fun <T> addSource(mediatorLiveData: MediatorLiveData<T>, liveData: LiveData<T>) {
        mediatorLiveData.addSource(liveData) {
            mediatorLiveData.value = it
        }
    }

}