package com.natasaandzic.moviedatabase.screens.bottom

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.navigation.Screen
import com.natasaandzic.moviedatabase.viewmodel.NowPlayingViewModel
import com.natasaandzic.moviedatabase.viewmodel.PopularMoviesViewModel
import com.natasaandzic.moviedatabase.viewmodel.TopRatedMoviesViewModel
import com.natasaandzic.moviedatabase.viewmodel.UpcomingMoviesViewModel
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
    val popularMovies by popularMoviesViewModel.filteredMovies.collectAsState()
    val popularMoviesLoading by popularMoviesViewModel.isLoading.collectAsState()

    val topRatedMovies by topRatedMoviesViewModel.movies.collectAsState()
    val topRatedMoviesLoading by topRatedMoviesViewModel.isLoading.collectAsState()

    val nowPlayingMovies by nowPlayingViewModel.movies.collectAsState()
    val nowPlayingMoviesLoading by nowPlayingViewModel.isLoading.collectAsState()

    val upcomingMoviesViewModel: UpcomingMoviesViewModel = hiltViewModel()
    val upcomingMovies by upcomingMoviesViewModel.movies.collectAsState()
    val upcomingMoviesLoading by upcomingMoviesViewModel.isLoading.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        HorizontalCategoryItem(
            title = "Popular movies",
            route = "popular",
            navController = navController,
            movies = popularMovies,
            listLoading = popularMoviesLoading,
            onEndReached = { popularMoviesViewModel.loadNextPage() },
            onMovieClicked = onMovieClicked
        )

        HorizontalCategoryItem(
            title = "Top rated movies",
            route = "top_rated",
            navController = navController,
            movies = topRatedMovies,
            listLoading = topRatedMoviesLoading,
            onEndReached = { topRatedMoviesViewModel.loadNextPage() },
            onMovieClicked = onMovieClicked
        )

        HorizontalCategoryItem(
            title = "Now playing",
            route = "now_playing",
            navController = navController,
            movies = nowPlayingMovies,
            listLoading = nowPlayingMoviesLoading,
            onEndReached = { nowPlayingViewModel.loadNextPage() },
            onMovieClicked = onMovieClicked
        )

        HorizontalCategoryItem(
            title = "Upcoming movies",
            route = "upcoming",
            navController = navController,
            movies = upcomingMovies,
            listLoading = upcomingMoviesLoading,
            onEndReached = { upcomingMoviesViewModel.loadNextPage() },
            onMovieClicked = onMovieClicked
        )
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

@Composable
fun HorizontalCategoryItem(
    title: String, route: String, navController: NavHostController, movies: List<Movie>,
    listLoading: Boolean, onEndReached: () -> Unit, onMovieClicked: (Int) -> Unit
) {
    Text(
        text = title,
        modifier = Modifier
            .clickable {
                navController.navigate(route)
            }
            .padding(16.dp),
        style = MaterialTheme.typography.titleLarge
    )

    HorizontalMovieList(
        movies = movies,
        isLoading = listLoading,
        onEndReached = onEndReached,
        onMovieClicked
    )

    if (listLoading) {
        CircularProgressIndicator(Modifier.padding(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val title = when (currentRoute) {
        Screen.Home.route -> "MovieDB"
        Screen.Search.route -> "Search"
        Screen.Favorites.route -> "Favorites"
        Screen.Genres.route -> "Genres"
        Screen.GenreMoviesScreen.route -> "Movies"
        Screen.NowPlaying.route -> "Now playing"
        Screen.Popular.route -> "Popular"
        Screen.TopRated.route -> "Top rated"
        Screen.Upcoming.route -> "Upcoming"
        Screen.Watchlist.route -> "Watchlist"
        else -> "MovieDB"
    }

    CenterAlignedTopAppBar(
        title = { Text(title) },
        actions = {
            if (currentRoute != Screen.Search.route) {
                IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }
    )
}
