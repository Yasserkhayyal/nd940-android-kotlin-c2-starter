package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.getSeventhDayDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.data.AsteroidsRepository
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch

class MainViewModel(repository: AsteroidsRepository) : ViewModel() {


    init {
        viewModelScope.launch {
            repository.refreshCachedAsteroids(getTodayDate(), getSeventhDayDate())
            repository.getImageOfTheDay()
        }
    }

    val asteroids = repository.asteroids
    val imageOfTheDay = repository.imageOfTheDay
    val todayDate
        get() = MutableLiveData(getTodayDate())

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                val asteroidsDatabase = getDatabase(app)
                val asteroidsRepository = AsteroidsRepository(asteroidsDatabase)
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(asteroidsRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}