package com.natasaandzic.moviedatabase.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.natasaandzic.moviedatabase.navigation.BottomNavItem
import com.natasaandzic.moviedatabase.navigation.BottomNavigationBar
import com.natasaandzic.moviedatabase.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val onMovieClicked: (Int) -> Unit = { id ->
        navController.navigate(Screen.MovieDetails.createRoute(id))
    }

    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Search", Screen.Search.route, Icons.Default.Search),
        BottomNavItem("Favorites", Screen.Favorites.route, Icons.Default.Favorite),
        BottomNavItem("Genres", Screen.Genres.route, Icons.Default.List)
    )
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value

    val routesWithoutTopBar = listOf(Screen.MovieDetails.route)
    val currentRoute = currentBackStackEntry?.destination?.route


    Scaffold(
        topBar = {
            if (currentRoute !in routesWithoutTopBar) {
                AppTopBar(navController)
            }
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = bottomNavItems
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController, onMovieClicked = onMovieClicked)
            }
            composable(Screen.NowPlaying.route) {
                NowPlayingScreen(onMovieClicked = onMovieClicked)
            }
            composable(Screen.Upcoming.route) {
                UpcomingMoviesScreen(onMovieClicked = onMovieClicked)
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
                GenresScreen(onGenreClicked = { genreId ->
                    navController.navigate(Screen.GenreMoviesScreen.createRoute(genreId))
                })
            }
            composable(
                route = Screen.GenreMoviesScreen.route,
                arguments = listOf(navArgument("genreId") { type = NavType.IntType })
            ) { backStackEntry ->
                val genreId = backStackEntry.arguments?.getInt("genreId") ?: return@composable
                GenreMoviesScreen(
                    genreId = genreId,
                    onMovieClicked = onMovieClicked
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(onMovieClicked = onMovieClicked)
            }
        }
    }
}
