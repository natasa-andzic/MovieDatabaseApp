package com.natasaandzic.moviedatabase.screens.main

import android.util.Log
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.natasaandzic.moviedatabase.R
import com.natasaandzic.moviedatabase.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
        }
    }

    AndroidView(factory = { context ->
        ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(context)
                .asGif()
                .load(R.raw.gif_movie)
                .into(this)
        }
    })
}

