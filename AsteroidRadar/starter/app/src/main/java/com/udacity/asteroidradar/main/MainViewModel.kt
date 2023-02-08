package com.udacity.asteroidradar.main

import android.app.Application
import android.icu.util.MeasureUnit.WEEK
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.repository.FilterList
import com.udacity.asteroidradar.repository.LoadingOrCompleteStatus
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel( application: Application) : ViewModel() {

    private val database = AsteroidDatabase.getInstance(application).asteroidDao
    private val repository = Repository(database)

    private var _photo = MutableLiveData<PictureOfDay>()
    val photo: LiveData<PictureOfDay>
        get() = _photo

    private val _asteroids = repository.asteroids
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    val loadingStatus: LiveData<LoadingOrCompleteStatus> = repository.loadingOrCompleteStatus

    private val _selectedAsteroid = MutableLiveData<Asteroid?>()
    val selectedAsteroid: LiveData<Asteroid?>
        get() = _selectedAsteroid

    fun onSelectAsteroid(asteroid: Asteroid) {
        _selectedAsteroid.value = asteroid
    }

    fun onClearSelected() {
        _selectedAsteroid.value = null
    }

    init {
        getPhotoOfTheDay()
        start()
    }

    fun start (){
        viewModelScope.launch {
            repository.setSelector(FilterList.Week)
            repository.refreshList()
        }
    }

    fun selectFilter(selection: FilterList) {
        viewModelScope.launch {
            repository.setSelector(selection)
        }
    }

    private fun getPhotoOfTheDay() {
        viewModelScope.launch {
            _photo.value = AsteroidApi.retrofitService.getPhotoOfTheDay()
        }
    }
}