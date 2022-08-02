package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.database.PictureOfDay
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface NASAApi {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query(value = "start_date") startDate: String,
        @Query(value = "end_date") endDate: String,
        @Query(value = "api_key") apiKey: String = BuildConfig.API_KEY
    ): ResponseBody


    @GET("planetary/apod")
    suspend fun getImageOfTheDay(@Query(value = "api_key") apiKey: String = BuildConfig.API_KEY): PictureOfDay

}