package com.androiddevs.mvvmnewsapp.ui.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import androidx.room.Query
import com.androiddevs.mvvmnewsapp.ui.Repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.app.NewsApplication
import com.androiddevs.mvvmnewsapp.ui.models.Article
import com.androiddevs.mvvmnewsapp.ui.models.NewsResponse
import com.androiddevs.mvvmnewsapp.ui.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app: Application,
                    val newsRepository : NewsRepository
) :  AndroidViewModel(app) {
    private var _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews: LiveData<Resource<NewsResponse>>
        get() = _breakingNews

    val breakingNewsPage = 1

    private var _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews: LiveData<Resource<NewsResponse>>
        get() = _searchNews

    val searchNewsPage = 1

    init {
        getBreakingNews("eg")
    }

    fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun getSearchNews(searchQuery : String) = viewModelScope.launch {
       safeSearchNewsCall(searchQuery)
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {

        _searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                _searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                _searchNews.postValue(Resource.Erorr("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> _searchNews.postValue(Resource.Erorr("Network Failure"))
                else -> _searchNews.postValue(Resource.Erorr("Conversion Error"))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        _breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRepository.getBreackingNews(countryCode, breakingNewsPage)
                _breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                _breakingNews.postValue(Resource.Erorr("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> _breakingNews.postValue(Resource.Erorr("Network Failure"))
                else -> _breakingNews.postValue(Resource.Erorr("Conversion Error"))
            }
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Succsess(it)
            }
        }
        return Resource.Erorr(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Succsess(it)
            }
        }
        return Resource.Erorr(response.message())
    }

    fun saveArticle(article : Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getSaveNews() = newsRepository.getSavedNews()

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}