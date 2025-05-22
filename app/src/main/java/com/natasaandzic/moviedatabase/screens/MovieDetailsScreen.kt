package com.natasaandzic.moviedatabase.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Scaffold
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
import com.natasaandzic.moviedatabase.viewmodel.MovieDetailsViewModel
import kotlinx.coroutines.delay

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val movie by viewModel.movie.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(movieId) {
        viewModel.getMovie(movieId)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        movie?.let { movie ->
            Scaffold(
                floatingActionButton = {
                    FavoriteButton(
                        isFavorite = movie.isFavorite,
                        onClick = {
                            viewModel.toggleFavorite(movie)
                        })}
                    ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
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

                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "Released: ${movie.release_date}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
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
                                text = movie.vote_average.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Overview",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = movie.overview,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp
                        )
                    }
                }
                } ?: run {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Movie not found")
                    }
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

        // Reset scale after animation
        LaunchedEffect(animate) {
            if (animate) {
                delay(200)
                animate = false
            }
        }
    }
