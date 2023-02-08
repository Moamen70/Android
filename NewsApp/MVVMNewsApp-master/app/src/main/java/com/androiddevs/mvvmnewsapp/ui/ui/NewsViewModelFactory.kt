package com.androiddevs.mvvmnewsapp.ui.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.mvvmnewsapp.ui.Repository.NewsRepository

class NewsViewModelFactory(private val app : Application,private val newsRepository : NewsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
           return NewsViewModel(app ,newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}