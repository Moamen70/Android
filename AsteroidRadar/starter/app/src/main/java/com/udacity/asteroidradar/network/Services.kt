package com.udacity.asteroidradar.network



import androidx.core.app.NotificationCompat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.Date
import com.udacity.asteroidradar.PictureOfDay
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.StreamTokenizer


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AsteroidApiService {

    @GET("planetary/apod")
    suspend fun getPhotoOfTheDay(@Query("api_key")  key: String = Constants.Key) :PictureOfDay

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") sDate : String = Date.getDateOfToday(),
        @Query("end_date") eDate : String = Date.getEndDate(),
        @Query("api_key")  key: String = Constants.Key
    ) :String
}

object AsteroidApi {
    val retrofitService : AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}