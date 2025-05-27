package com.natasaandzic.moviedatabase.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import com.natasaandzic.moviedatabase.R
import com.natasaandzic.moviedatabase.navigation.BottomNavItem
import com.natasaandzic.moviedatabase.navigation.BottomNavigationBar
import com.natasaandzic.moviedatabase.navigation.Screen
import com.natasaandzic.moviedatabase.screens.GenreMoviesScreen
import com.natasaandzic.moviedatabase.screens.MovieDetailsScreen
import com.natasaandzic.moviedatabase.screens.NowPlayingScreen
import com.natasaandzic.moviedatabase.screens.PopularMoviesScreen
import com.natasaandzic.moviedatabase.screens.TopRatedMoviesScreen
import com.natasaandzic.moviedatabase.screens.UpcomingMoviesScreen
import com.natasaandzic.moviedatabase.screens.bottom.AppTopBar
import com.natasaandzic.moviedatabase.screens.bottom.FavoritesScreen
import com.natasaandzic.moviedatabase.screens.bottom.GenresScreen
import com.natasaandzic.moviedatabase.screens.bottom.HomeScreen
import com.natasaandzic.moviedatabase.screens.bottom.SearchScreen
import com.natasaandzic.moviedatabase.screens.bottom.WatchlistScreen
import com.natasaandzic.moviedatabase.ui.theme.Mint1

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
        BottomNavItem("Home", Screen.Home.route, R.drawable.ic_home),
        BottomNavItem("Search", Screen.Search.route, R.drawable.ic_search),
        BottomNavItem("Favorites", Screen.Favorites.route, R.drawable.ic_favorite),
        BottomNavItem("Watchlist", Screen.Watchlist.route, R.drawable.ic_watchlist),
        BottomNavItem("Genres", Screen.Genres.route, R.drawable.ic_genres)
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
        }, containerColor = Mint1
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(navController)
            }
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
            composable(Screen.Watchlist.route) {
                WatchlistScreen(onMovieClicked = onMovieClicked)
            }
        }
    }
}
