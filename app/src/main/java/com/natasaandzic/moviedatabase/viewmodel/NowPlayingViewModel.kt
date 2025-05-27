package com.natasaandzic.moviedatabase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.data.RatingFilter
import com.natasaandzic.moviedatabase.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies = _movies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _ratingFilter = MutableStateFlow(RatingFilter.ALL)
    val ratingFilter: StateFlow<RatingFilter> = _ratingFilter

    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE

    val filteredMovies: StateFlow<List<Movie>> =
        combine(_movies, _ratingFilter) { allMovies, filter ->
            allMovies.filter { it.vote_average >= filter.minRating }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setRatingFilter(filter: RatingFilter) {
        _ratingFilter.value = filter
    }

    fun loadNextPage() {
        if (_isLoading.value || currentPage > totalPages) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getNowPlayingMoviesPaged(currentPage)
                _movies.value += response.results
                totalPages = response.total_pages
                currentPage++
            } catch (_: Exception) {
            }
            _isLoading.value = false
        }
    }

    init {
        loadNextPage()
    }

    fun refresh() {
        currentPage = 1
        _movies.value = emptyList()
        loadNextPage()
    }
}
