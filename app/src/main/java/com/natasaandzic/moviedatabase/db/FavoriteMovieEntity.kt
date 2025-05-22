package com.natasaandzic.moviedatabase.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val release_date: String?,
    val vote_average: Double
)
