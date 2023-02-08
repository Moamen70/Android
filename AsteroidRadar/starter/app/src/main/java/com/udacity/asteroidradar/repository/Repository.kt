package com.udacity.asteroidradar.repository

import android.content.Context
import android.net.Network
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.io.StreamTokenizer


enum class FilterList {
    TODAY, Week, ALL
}

enum class LoadingOrCompleteStatus {
    LOADING, COMPLETE
}

class Repository(
    val db: AsteroidDao
) {
    private val selector = MutableLiveData<FilterList>()

    private val _loadingOrCompleteStatus = MutableLiveData<LoadingOrCompleteStatus>(LoadingOrCompleteStatus.LOADING)
    val loadingOrCompleteStatus: LiveData<LoadingOrCompleteStatus>
        get() = _loadingOrCompleteStatus

    fun setSelector(selection: FilterList) {
        selector.value = selection
    }

    val asteroids: LiveData<List<Asteroid>> = selector.switchMap { selector ->
        liveData(context = Dispatchers.IO) {
            _loadingOrCompleteStatus.postValue(LoadingOrCompleteStatus.LOADING)
            emitSource(filterAsteroids(selector))
            _loadingOrCompleteStatus.postValue(LoadingOrCompleteStatus.COMPLETE)
        }
    }

    private fun filterAsteroids(selection: FilterList): LiveData<List<Asteroid>> {

        return when (selection) {
            FilterList.ALL -> db.getAllAsteroids()

            FilterList.TODAY -> db.getAsteroidForToday()

            else -> db.getAsteroidForWeek()
        }.map { it.asDomainModel() }
    }

    suspend fun refreshList() {
        withContext(Dispatchers.IO) {
            _loadingOrCompleteStatus.postValue(LoadingOrCompleteStatus.LOADING)
            try {
                val asteroidsList =
                    getAstroidsFromApi(AsteroidApi.retrofitService.getAsteroids())

                val dbArray = asteroidsList.asDatabaseModel()
                db.insertAll(*dbArray)
            } catch (e: Exception) {
                Timber.i("no internet connection ${e.message}")
            }
        }
    }

    private fun getAstroidsFromApi(jsonFromApi: String): List<Asteroid> {
        val jsonObject = JSONObject(jsonFromApi)
        return parseAsteroidsJsonResult(jsonObject)
    }
}