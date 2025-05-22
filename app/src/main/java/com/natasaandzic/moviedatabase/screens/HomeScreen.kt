package com.natasaandzic.moviedatabase.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.viewmodel.NowPlayingViewModel
import com.natasaandzic.moviedatabase.viewmodel.PopularMoviesViewModel
import com.natasaandzic.moviedatabase.viewmodel.TopRatedMoviesViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun HomeScreen(
    navController: NavHostController,
    onMovieClicked: (Int) -> Unit,
    popularMoviesViewModel: PopularMoviesViewModel = hiltViewModel(),
    topRatedMoviesViewModel: TopRatedMoviesViewModel = hiltViewModel(),
    nowPlayingViewModel: NowPlayingViewModel = hiltViewModel()
) {
    val popularMovies by popularMoviesViewModel.movies.collectAsState()
    val topRatedMovies by topRatedMoviesViewModel.movies.collectAsState()
    val popularMoviesLoading by popularMoviesViewModel.isLoading.collectAsState()
    val topRatedMoviesLoading by topRatedMoviesViewModel.isLoading.collectAsState()

    val nowPlayingMovies by nowPlayingViewModel.movies.collectAsState()
    val nowPlayingMoviesLoading by nowPlayingViewModel.isLoading.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {

        Text(
            text = "Popular Movies",
            modifier = Modifier
                .clickable {
                    navController.navigate("popular")
                }
                .padding(48.dp),
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalMovieList(
            movies = popularMovies,
            isLoading = popularMoviesLoading,
            onEndReached = { popularMoviesViewModel.loadNextPage() },
            onMovieClicked
        )

        if (popularMoviesLoading) {
            CircularProgressIndicator(Modifier.padding(16.dp))
        }

        Text(
            text = "Top Rated Movies",
            modifier = Modifier
                .clickable {
                    navController.navigate("top_rated")
                }
                .padding(48.dp),
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalMovieList(
            movies = topRatedMovies,
            isLoading = topRatedMoviesLoading,
            onEndReached = { topRatedMoviesViewModel.loadNextPage() },
            onMovieClicked
        )

        if (topRatedMoviesLoading) {
            CircularProgressIndicator(Modifier.padding(16.dp))
        }

        Text(
            text = "Now Playing Movies",
            modifier = Modifier
                .clickable {
                    navController.navigate("now_playing")
                }
                .padding(48.dp),
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalMovieList(
            movies = nowPlayingMovies,
            isLoading = nowPlayingMoviesLoading,
            onEndReached = { nowPlayingViewModel.loadNextPage() },
            onMovieClicked
        )

        if (nowPlayingMoviesLoading) {
            CircularProgressIndicator(Modifier.padding(16.dp))
        }

    }
}

@Composable
fun HorizontalMovieList(
    movies: List<Movie>,
    isLoading: Boolean,
    onEndReached: () -> Unit,
    onMovieClicked: (Int) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, isLoading, movies.size) {
        snapshotFlow { listState.layoutInfo }
            .map { it.visibleItemsInfo.lastOrNull()?.index ?: -1 }
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex >= movies.size - 3 && !isLoading) {
                    onEndReached()
                }
            }
    }

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie, onMovieClicked)
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onMovieClicked: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable { onMovieClicked(movie.id) }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
