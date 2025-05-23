package com.natasaandzic.moviedatabase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.data.Filter
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingMoviesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 1

    private val _filter = MutableStateFlow(Filter.ALL)
    val filter: StateFlow<Filter> = _filter


    init {
        loadNextPage()
    }

    fun loadNextPage() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getUpcomingMovies(page = currentPage)
                _movies.value = _movies.value + response.results
                currentPage++
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshUpcoming() {
        currentPage = 1
        _movies.value = emptyList()
        loadNextPage()
    }

    fun setFilter(newFilter: Filter) {
        _filter.value = newFilter
        currentPage = 1
        _movies.value = emptyList()
        loadNextPage()
    }

}
