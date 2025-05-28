package com.natasaandzic.moviedatabase.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.db.MovieDao
import com.natasaandzic.moviedatabase.db.FavoriteMovieEntity
import com.natasaandzic.moviedatabase.db.WatchlistMovieEntity
import com.natasaandzic.moviedatabase.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val dao: MovieDao
) : ViewModel() {

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _trailerKey = MutableStateFlow<String?>(null)
    val trailerKey: StateFlow<String?> = _trailerKey

    fun getTrailer(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMovieVideos(movieId)
                val trailer = response.results.firstOrNull {
                    it.site == "YouTube" && it.type == "Trailer"
                }
                _trailerKey.value = trailer?.key
            } catch (e: Exception) {
                _trailerKey.value = null
            }
        }
    }

    fun getMovie(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                refreshMovie(movieId)
                getTrailer(movieId)
            } catch (e: Exception) {
                Log.e("MovieDetailsViewModel", "Error loading movie", e)
                _movie.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            if (!movie.isFavorite) {
                dao.insertFavorite(movie.toFavoriteEntity())
            } else {
                dao.deleteFavorite(movie.toFavoriteEntity())
            }
            refreshMovie(movie.id)
        }
    }

    fun toggleWatchlist(movie: Movie) {
        viewModelScope.launch {
            if (!movie.isInWatchlist) {
                dao.insertInWatchlist(movie.toWatchlistEntity())
            } else {
                dao.deleteFromWatchlist(movie.toWatchlistEntity())
            }
            refreshMovie(movie.id)
        }
    }

    private suspend fun refreshMovie(movieId: Int) {
        val response = repository.getMovie(movieId)
        val isFavorite = dao.isFavorite(movieId)
        val isInWatchlist = dao.isInWatchlist(movieId)
        _movie.value = response.copy(
            isFavorite = isFavorite,
            isInWatchlist = isInWatchlist
        )
    }
}


fun Movie.toFavoriteEntity(): FavoriteMovieEntity {
    return FavoriteMovieEntity(
        id = id,
        title = title,
        overview = overview,
        poster_path = poster_path,
        release_date = release_date,
        vote_average = vote_average
    )
}

fun Movie.toWatchlistEntity(): WatchlistMovieEntity {
    return WatchlistMovieEntity(
        id = id,
        title = title,
        overview = overview,
        poster_path = poster_path,
        release_date = release_date,
        vote_average = vote_average
    )
}
