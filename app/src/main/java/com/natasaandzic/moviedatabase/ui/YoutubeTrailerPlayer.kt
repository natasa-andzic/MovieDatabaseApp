package com.natasaandzic.moviedatabase.ui

import android.content.Intent
import android.widget.ImageView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.natasaandzic.moviedatabase.screens.FullscreenPlayerActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController

@Composable
fun TrailerPlayer(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier.aspectRatio(16f / 9f),
        factory = { ctx ->
            YouTubePlayerView(ctx).apply {
                enableAutomaticInitialization = false

                val listener = object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)

                        val uiController = DefaultPlayerUiController(this@apply, youTubePlayer)
                        setCustomPlayerUi(uiController.rootView)
                        uiController.showFullscreenButton(true)

                        val fullscreenButton = uiController.rootView.findViewById<ImageView>(
                            com.pierfrancescosoffritti.androidyoutubeplayer.R.id.fullscreen_button
                        )

                        fullscreenButton.setOnClickListener {
                            val intent = Intent(ctx, FullscreenPlayerActivity::class.java)
                            intent.putExtra("videoId", videoId)
                            ctx.startActivity(intent)
                        }
                    }
                }

                initialize(listener)
                lifecycleOwner.lifecycle.addObserver(this)
            }
        }
    )
}
