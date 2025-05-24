package com.natasaandzic.moviedatabase.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.natasaandzic.moviedatabase.viewmodel.PopularMoviesViewModel

@Composable
fun PopularMoviesScreen(
    viewModel: PopularMoviesViewModel = hiltViewModel(),
    onMovieClicked: (Int) -> Unit
) {
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold() { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            MovieRatingChips(viewModel)

            MoviesScreen(
                title = "Popular Movies",
                movies = movies,
                isLoading = isLoading,
                onLoadMore = { viewModel.loadNextPage() },
                onMovieClicked = onMovieClicked
            )
        }

    }
}