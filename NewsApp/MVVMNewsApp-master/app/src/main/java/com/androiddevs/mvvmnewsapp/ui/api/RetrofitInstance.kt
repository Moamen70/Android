package com.androiddevs.mvvmnewsapp.ui.api

import com.androiddevs.mvvmnewsapp.ui.util.Constants.Companion.Base_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class RetrofitInstance {
    companion object{

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()).baseUrl(Base_URL).build()

        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }


}