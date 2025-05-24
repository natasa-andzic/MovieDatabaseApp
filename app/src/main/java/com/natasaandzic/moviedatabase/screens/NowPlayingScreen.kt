package com.natasaandzic.moviedatabase.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.natasaandzic.moviedatabase.viewmodel.NowPlayingViewModel

@Composable
fun NowPlayingScreen(
    viewModel: NowPlayingViewModel = hiltViewModel(),
    onMovieClicked: (Int) -> Unit
) {
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    //MovieRatingChips(viewModel)

    MoviesScreen(
        title = "Now Playing",
        movies = movies,
        isLoading = isLoading,
        onLoadMore = { viewModel.loadNextPage() },
        onMovieClicked = onMovieClicked
    )

}