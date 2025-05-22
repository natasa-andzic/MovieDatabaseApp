package com.natasaandzic.moviedatabase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.natasaandzic.moviedatabase.screens.HomeScreen
import com.natasaandzic.moviedatabase.screens.MovieDetailsScreen
import com.natasaandzic.moviedatabase.screens.PopularMoviesScreen
import com.natasaandzic.moviedatabase.screens.TopRatedMoviesScreen

@Composable
fun MovieAppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, onMovieClicked = { id ->
                navController.navigate("movie_details/$id")
            })
        }
        composable("now_playing") {
            //NowPlayingScreen()
        }
        composable("popular") {
            PopularMoviesScreen()
        }
        composable("movie_details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            MovieDetailsScreen(movieId = id)
        }

        composable("top_rated") {
            TopRatedMoviesScreen()
        }
    }
}

