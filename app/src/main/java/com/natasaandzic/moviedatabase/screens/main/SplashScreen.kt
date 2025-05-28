package com.natasaandzic.moviedatabase.screens.main

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.bumptech.glide.Glide
import com.natasaandzic.moviedatabase.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(factory = { context ->
            ImageView(context).apply {
                layoutParams = ViewGroup.LayoutParams(500, 500)
                scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(context)
                    .asGif()
                    .load(R.raw.gif_movie)
                    .into(this)
            }
        })
    }
}
