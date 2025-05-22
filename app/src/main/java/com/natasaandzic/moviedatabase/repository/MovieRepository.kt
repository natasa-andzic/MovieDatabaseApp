package com.natasaandzic.moviedatabase.repository

import android.util.Log
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.data.MovieResponse
import com.natasaandzic.moviedatabase.data.NowPlayingResponse
import com.natasaandzic.moviedatabase.network.MovieApiService
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieApiService: MovieApiService) {

    suspend fun getPopularMoviesPaged(page: Int): MovieResponse {
        return movieApiService.getPopularMovies(page)
    }

    suspend fun getNowPlayingMoviesPaged(page: Int): NowPlayingResponse {
        return movieApiService.getNowPlayingMovies(page)
    }

    suspend fun getMovie(movieId: Int): Movie {
        return movieApiService.getMovie(movieId)
    }

    suspend fun getTopRatedMovies(page: Int): MovieResponse {
        return movieApiService.getTopRatedMovies(page)
    }

    suspend fun searchMovies(query: String): List<Movie> {
        return movieApiService.searchMovies(query).results
    }


}