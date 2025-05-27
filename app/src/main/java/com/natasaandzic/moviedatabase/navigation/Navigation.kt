package com.natasaandzic.moviedatabase.navigation

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object NowPlaying : Screen("now_playing")
    object Popular : Screen("popular")
    object TopRated : Screen("top_rated")
    object Upcoming : Screen("upcoming")

    object MovieDetails : Screen("movie_details/{id}") {
        fun createRoute(id: Int) = "movie_details/$id"
    }

    object Search : Screen("search")
    object Genres : Screen("genres")
    object Favorites : Screen("favorites")
    object Watchlist: Screen("watchlist")

    object GenreMoviesScreen : Screen("genres/{genreId}") {
        fun createRoute(genreId: Int) = "genres/$genreId"
    }
}