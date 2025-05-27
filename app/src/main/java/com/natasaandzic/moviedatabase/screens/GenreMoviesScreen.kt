package com.natasaandzic.moviedatabase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.natasaandzic.moviedatabase.viewmodel.MovieGenresViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreMoviesScreen(
    genreId: Int,
    viewModel: MovieGenresViewModel = hiltViewModel(),
    onMovieClicked: (Int) -> Unit
) {
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(genreId) {
        viewModel.loadMoviesByGenre(genreId)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MoviePoster(movie = movie, onClick = { onMovieClicked(movie.id) })
        }

        if (isLoading) {
            item(span = { GridItemSpan(3) }) {
                CircularProgressIndicator(Modifier.padding(16.dp))
            }
        }
    }
}
