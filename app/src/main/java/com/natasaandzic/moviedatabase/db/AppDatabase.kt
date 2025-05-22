package com.natasaandzic.moviedatabase.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.natasaandzic.moviedatabase.db.MovieDao

@Database(entities = [FavoriteMovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}