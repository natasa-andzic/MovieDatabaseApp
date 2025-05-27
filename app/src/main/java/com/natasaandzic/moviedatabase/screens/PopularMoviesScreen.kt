package com.natasaandzic.moviedatabase.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.natasaandzic.moviedatabase.viewmodel.PopularMoviesViewModel

@Composable
fun PopularMoviesScreen(
    viewModel: PopularMoviesViewModel = hiltViewModel(),
    onMovieClicked: (Int) -> Unit
) {
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    Column {

        MovieRatingChips(viewModel)

        MoviesScreen(
            movies = movies,
            isLoading = isLoading,
            onLoadMore = { viewModel.loadNextPage() },
            onMovieClicked = onMovieClicked
        )
    }

}
