package com.natasaandzic.moviedatabase.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        if (newQuery.length >= 2) {
            searchMovies(newQuery)
        } else {
            _results.value = emptyList()
        }
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _results.value = repository.searchMovies(query)
            } catch (e: Exception) {
                Log.e("Search", "Error searching movies", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
