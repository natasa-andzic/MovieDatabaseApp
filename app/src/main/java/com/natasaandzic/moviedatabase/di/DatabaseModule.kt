package com.natasaandzic.moviedatabase.di

import android.content.Context
import androidx.room.Room
import com.natasaandzic.moviedatabase.db.AppDatabase
import com.natasaandzic.moviedatabase.db.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "movies_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()
}
