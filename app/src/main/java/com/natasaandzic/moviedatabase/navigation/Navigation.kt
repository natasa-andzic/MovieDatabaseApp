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
import com.natasaandzic.moviedatabase.screens.HomeScreen
import com.natasaandzic.moviedatabase.screens.MovieDetailsScreen
import com.natasaandzic.moviedatabase.screens.NowPlayingScreen
import com.natasaandzic.moviedatabase.screens.PopularMoviesScreen
import com.natasaandzic.moviedatabase.screens.SearchScreen
import com.natasaandzic.moviedatabase.screens.TopRatedMoviesScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NowPlaying : Screen("now_playing")
    object Popular : Screen("popular")
    object TopRated : Screen("top_rated")
    object MovieDetails : Screen("movie_details/{id}") {
        fun createRoute(id: Int) = "movie_details/$id"
    }

    object Search : Screen("search")
    object Profile : Screen("profile")
    object Genres : Screen("genres")
    object Favorites : Screen("favorites")
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
            //GenresScreen()
        }
        composable(Screen.Profile.route) {
            //ProfileScreen()
        }
        composable(Screen.Search.route) {
            SearchScreen(onMovieClicked = onMovieClicked)
        }
    }
}