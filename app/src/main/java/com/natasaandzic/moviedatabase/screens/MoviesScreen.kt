package com.natasaandzic.moviedatabase.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.data.RatingFilter
import com.natasaandzic.moviedatabase.viewmodel.PopularMoviesViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun MoviesScreen(
    title: String,
    movies: List<Movie>,
    isLoading: Boolean,
    onLoadMore: () -> Unit,
    onMovieClicked: (Int) -> Unit
) {
    val listState = rememberLazyGridState()

    LaunchedEffect(listState, isLoading, movies.size) {
        snapshotFlow { listState.layoutInfo }
            .map { it.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .collect { lastVisible ->
                if (lastVisible >= movies.size - 6 && !isLoading) {
                    onLoadMore()
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = title,
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.titleLarge
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = listState,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(movies) { movie ->
                MoviePoster(movie = movie, onClick = { onMovieClicked(movie.id) })
            }

            if (isLoading) {
                item(span = { GridItemSpan(3) }) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun MoviePoster(movie: Movie, onClick: () -> Unit) {
    AsyncImage(
        model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
        contentDescription = movie.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
    )
}

@Composable
fun MovieRatingChips(popularMoviesViewModel: PopularMoviesViewModel) {
    val ratingFilter by popularMoviesViewModel.ratingFilter.collectAsState()

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        RatingFilter.values().forEach { filter ->
            item {
                AnimatedFilterChip(
                    text = filter.label,
                    selected = ratingFilter == filter,
                    onClick = { popularMoviesViewModel.setRatingFilter(filter) }
                )
            }
        }
    }
}