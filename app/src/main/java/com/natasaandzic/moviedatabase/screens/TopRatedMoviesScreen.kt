package com.natasaandzic.moviedatabase.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.natasaandzic.moviedatabase.viewmodel.TopRatedMoviesViewModel

@Composable
fun TopRatedMoviesScreen(
    viewModel: TopRatedMoviesViewModel = hiltViewModel(),
    onMovieClicked: (Int) -> Unit
) {
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading && movies.isEmpty()) {
        CircularProgressIndicator()
    } else
    MoviesScreen(
        movies = movies,
        isLoading = isLoading,
        onLoadMore = { viewModel.loadNextPage() },
        onMovieClicked = onMovieClicked
    )
}
