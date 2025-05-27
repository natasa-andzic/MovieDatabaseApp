package com.natasaandzic.moviedatabase.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.data.RatingFilter
import com.natasaandzic.moviedatabase.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _ratingFilter = MutableStateFlow(RatingFilter.ALL)
    val ratingFilter: StateFlow<RatingFilter> = _ratingFilter

    val filteredMovies: StateFlow<List<Movie>> = combine(_movies, _ratingFilter) { allMovies, filter ->
        val lowerBound = filter.minRating
        val upperBound = lowerBound + 1
        allMovies.filter { it.vote_average.toFloat() in lowerBound..<upperBound }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE

    fun setRatingFilter(filter: RatingFilter) {
        _ratingFilter.value = filter
    }

    fun loadNextPage() {
        if (_isLoading.value || currentPage > totalPages) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getPopularMoviesPaged(currentPage)
                _movies.value = _movies.value + response.results
                totalPages = response.total_pages
                currentPage++
            } catch (e: Exception) {
                Log.e("Pagination", "Error loading page", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    init {
        loadNextPage()
    }
}
