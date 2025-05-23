package com.natasaandzic.moviedatabase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _results = MutableStateFlow<List<Movie>>(emptyList())
    val results: StateFlow<List<Movie>> = _results

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _query
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { search ->
                    if (search.length >= 2) {
                        performSearch(search)
                    } else {
                        loadTrendingMovies(reset = true)
                    }
                }
        }
        loadTrendingMovies(reset = true)
    }


    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    private suspend fun performSearch(query: String) {
        _isLoading.value = true
        try {
            _results.value = repository.searchMovies(query)
        } finally {
            _isLoading.value = false
        }
    }

    private var currentPage = 1

    private fun loadTrendingMovies(reset: Boolean = false) {
        if (reset) currentPage = 1

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getPopularMoviesPaged(currentPage)
                val newMovies = response.results
                _results.value = if (reset) newMovies else _results.value + newMovies
                currentPage++
            } finally {
                _isLoading.value = false
            }
        }
    }


}