package com.natasaandzic.moviedatabase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.data.Genre
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieGenresViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 1
    private var currentGenreId: Int? = null

    init {
        loadGenres()
    }

    fun loadGenres() {
        viewModelScope.launch {
            try {
                _genres.value = repository.getGenres()
            } catch (e: Exception) {
            }
        }
    }

    private val _genreMap = MutableStateFlow<Map<Int, String>>(emptyMap())

    fun loadMoviesByGenre(genreId: Int, reset: Boolean = true) {
        if (reset) {
            currentPage = 1
            _movies.value = emptyList()
        }

        currentGenreId = genreId

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newMovies = repository.getMoviesByGenre(genreId, page = currentPage)
                _movies.value = _movies.value + newMovies
                currentPage++
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
}
