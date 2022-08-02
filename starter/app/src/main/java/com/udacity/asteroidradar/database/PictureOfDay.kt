package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class PictureOfDay(
    @PrimaryKey
    val url: String,
    @Json(name = "media_type") val mediaType: String,
    val title: String
)