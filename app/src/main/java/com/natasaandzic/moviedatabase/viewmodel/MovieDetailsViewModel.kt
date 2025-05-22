package com.natasaandzic.moviedatabase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.db.MovieDao
import com.natasaandzic.moviedatabase.db.FavoriteMovieEntity
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

    fun getMovie(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getMovie(movieId)
                val isFav = dao.isFavorite(movieId)
                _movie.value = response.copy(isFavorite = isFav)
            } catch (e: Exception) {
                // Optionally log or handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val updated = movie.copy(isFavorite = !movie.isFavorite)
            if (updated.isFavorite) {
                dao.insertFavorite(updated.toFavoriteEntity())
            } else {
                dao.deleteFavorite(updated.toFavoriteEntity())
            }
            _movie.value = updated
        }
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

