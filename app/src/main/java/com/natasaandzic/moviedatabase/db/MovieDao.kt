package com.natasaandzic.moviedatabase.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovieEntity)

    @Delete
    suspend fun deleteFavorite(movie: FavoriteMovieEntity)

    @Query("SELECT * FROM favorite_movies")
    fun getAllFavorites(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean
}