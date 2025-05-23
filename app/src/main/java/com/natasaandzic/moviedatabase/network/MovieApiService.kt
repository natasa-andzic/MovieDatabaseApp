package com.natasaandzic.moviedatabase.network

import com.natasaandzic.moviedatabase.data.GenreResponse
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.data.MovieResponse
import com.natasaandzic.moviedatabase.data.NowPlayingResponse
import com.natasaandzic.moviedatabase.data.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
    ): MovieResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int): NowPlayingResponse

    @GET("genre/movie/list")
    suspend fun getMovieGenres(@Query("language") language: String = "en-US"): GenreResponse

    @GET("movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") movieId: Int): Movie

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String
    ): MovieResponse

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
    ): MovieResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
    ): VideoResponse


}