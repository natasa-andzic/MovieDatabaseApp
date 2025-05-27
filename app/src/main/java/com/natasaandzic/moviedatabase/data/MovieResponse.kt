package com.natasaandzic.moviedatabase.data

data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)


data class Movie(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>? = null,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false
)

data class NowPlayingResponse(
    val dates: DateRange,
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)

data class DateRange(
    val maximum: String,
    val minimum: String
)

data class VideoResponse(val results: List<Video>)

data class Video(
    val key: String,
    val site: String,
    val type: String,
    val name: String
)

