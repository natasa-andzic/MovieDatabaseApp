package com.natasaandzic.moviedatabase.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = rootNavController)
        }
        composable("main") {
            MainScreen()
        }
    }
}
