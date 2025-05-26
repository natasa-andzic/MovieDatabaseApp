package com.natasaandzic.moviedatabase.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.data.Filter
import com.natasaandzic.moviedatabase.data.Movie
import com.natasaandzic.moviedatabase.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
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

    @RequiresApi(Build.VERSION_CODES.O)
    val filteredMovies = combine(_movies, _filter) { movies, filter ->
        val now = LocalDate.now()

        movies.filter { movie ->
            val releaseDate = try {
                LocalDate.parse(movie.release_date)
            } catch (e: Exception) {
                null
            } ?: return@filter false

            when (filter) {
                Filter.ALL -> true

                Filter.CURRENT_MONTH -> {
                    val startOfMonth = now.withDayOfMonth(1)
                    val endOfMonth = now.withDayOfMonth(now.lengthOfMonth())
                    releaseDate in startOfMonth..endOfMonth
                }

                Filter.NEXT_MONTH -> {
                    val startOfNextMonth = now.plusMonths(1).withDayOfMonth(1)
                    val endOfNextMonth =
                        startOfNextMonth.withDayOfMonth(startOfNextMonth.lengthOfMonth())
                    releaseDate in startOfNextMonth..endOfNextMonth
                }
            }

        }
    }


    init {
        repeat(3) { loadNextPage() }
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

    fun setFilter(filter: Filter) {
        _filter.value = filter
        repeat(3) { loadNextPage() }
    }

}
