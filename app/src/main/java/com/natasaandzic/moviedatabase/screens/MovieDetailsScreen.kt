package com.natasaandzic.moviedatabase.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.natasaandzic.moviedatabase.formatReleaseDate
import com.natasaandzic.moviedatabase.ui.TrailerPlayer
import com.natasaandzic.moviedatabase.ui.theme.AccentColor
import com.natasaandzic.moviedatabase.ui.theme.AppTypography
import com.natasaandzic.moviedatabase.viewmodel.MovieDetailsViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val movieState by viewModel.movie.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val trailerKey by viewModel.trailerKey.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(movieId) {
        viewModel.getMovie(movieId)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        movieState?.let { movie ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Text(
                    text = movie.title,
                    style = AppTypography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                trailerKey?.let {
                    TrailerPlayer(videoId = it)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Date of release: ${formatReleaseDate(movie.release_date)}",
                    style = AppTypography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107)
                    )
                    Text(
                        text = if (String.format("%.2f", movie.vote_average) == "0.00")
                            "No ratings yet"
                        else
                            String.format("%.2f", movie.vote_average),
                        style = AppTypography.bodyLarge,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Overview",
                    style = AppTypography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = movie.overview,
                    style = AppTypography.bodyLarge,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    FavoriteButton(
                        isFavorite = movie.isFavorite,
                        onClick = {
                            viewModel.toggleFavorite(movie)
                        }
                    )

                    WatchlistButton(
                        isInWatchlist = movie.isInWatchlist,
                        onClick = {
                            viewModel.toggleWatchlist(movie)
                        }
                    )
                }
            }
        }
    }

    if (!isLoading && movieState == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Movie not found")
        }
    }
}


@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    var animate by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (animate) 1.4f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scale"
    )

    IconButton(
        onClick = {
            animate = true
            onClick()
        },
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFavorite) Color.Red else Color.Gray
        )
    }

    LaunchedEffect(animate) {
        if (animate) {
            delay(200)
            animate = false
        }
    }
}

@Composable
fun WatchlistButton(
    isInWatchlist: Boolean,
    onClick: () -> Unit
) {
    var animate by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (animate) 1.4f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scale"
    )

    IconButton(
        onClick = {
            animate = true
            onClick()
        },
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = if (isInWatchlist) Icons.Default.Star else Icons.Default.Star,
            contentDescription = "Watchlist",
            tint = if (isInWatchlist) AccentColor else Color.Gray
        )
    }

    LaunchedEffect(animate) {
        if (animate) {
            delay(200)
            animate = false
        }
    }
}
