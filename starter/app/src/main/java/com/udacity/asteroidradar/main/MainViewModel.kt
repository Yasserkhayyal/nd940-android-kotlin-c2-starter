package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.getSeventhDayDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.data.AsteroidsRepository
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch

class MainViewModel(repository: AsteroidsRepository) : ViewModel() {

    val asteroids = repository.asteroids
    val imageOfTheDay = repository.imageOfTheDay
    val todayDate
        get() = MutableLiveData(getTodayDate())
    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    init {
        viewModelScope.launch {
            _showLoading.value = true
            repository.refreshCachedAsteroids(getTodayDate(), getSeventhDayDate())
            repository.getImageOfTheDay()
            _showLoading.value = false
        }
    }

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