package com.natasaandzic.moviedatabase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.natasaandzic.moviedatabase.screens.FavoritesScreen
import com.natasaandzic.moviedatabase.screens.GenreMoviesScreen
import com.natasaandzic.moviedatabase.screens.GenresScreen
import com.natasaandzic.moviedatabase.screens.HomeScreen
import com.natasaandzic.moviedatabase.screens.MovieDetailsScreen
import com.natasaandzic.moviedatabase.screens.NowPlayingScreen
import com.natasaandzic.moviedatabase.screens.PopularMoviesScreen
import com.natasaandzic.moviedatabase.screens.SearchScreen
import com.natasaandzic.moviedatabase.screens.TopRatedMoviesScreen
import com.natasaandzic.moviedatabase.screens.UpcomingMoviesScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NowPlaying : Screen("now_playing")
    object Popular : Screen("popular")
    object TopRated : Screen("top_rated")
    object Upcoming : Screen("upcoming")

    object MovieDetails : Screen("movie_details/{id}") {
        fun createRoute(id: Int) = "movie_details/$id"
    }

    object Search : Screen("search")
    object Genres : Screen("genres") // for genre list screen
    object Favorites : Screen("favorites")

    object GenreMoviesScreen : Screen("genres/{genreId}") {
        fun createRoute(genreId: Int) = "genres/$genreId"
    }
}


@Composable
fun MovieAppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val onMovieClicked: (Int) -> Unit = { id ->
        navController.navigate(Screen.MovieDetails.createRoute(id))
    }

    NavHost(
        navController = navController, startDestination = Screen.Home.route, modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, onMovieClicked = onMovieClicked)
        }
        composable(Screen.NowPlaying.route) {
            NowPlayingScreen(onMovieClicked = onMovieClicked)
        }
        composable(Screen.Upcoming.route) {
            UpcomingMoviesScreen(
                onMovieClicked = onMovieClicked
            )
        }

        composable(Screen.Popular.route) {
            PopularMoviesScreen(onMovieClicked = onMovieClicked)
        }
        composable(Screen.TopRated.route) {
            TopRatedMoviesScreen(onMovieClicked = onMovieClicked)
        }
        composable(
            route = Screen.MovieDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            MovieDetailsScreen(movieId = id)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(onMovieClicked = onMovieClicked)
        }
        composable(Screen.Genres.route) {
            GenresScreen(
                onGenreClicked = { id ->
                    navController.navigate(Screen.GenreMoviesScreen.createRoute(id))
                }
            )
        }
        composable(
            route = Screen.GenreMoviesScreen.route,
            arguments = listOf(navArgument("genreId") { type = NavType.IntType })
        ) { backStackEntry ->
            val genreId = backStackEntry.arguments?.getInt("genreId") ?: return@composable
            GenreMoviesScreen(
                genreId = genreId,
                onMovieClicked = { movieId ->
                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(onMovieClicked = onMovieClicked)
        }
    }
}