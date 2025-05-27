package com.natasaandzic.moviedatabase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.db.MovieDao
import com.natasaandzic.moviedatabase.db.WatchlistMovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val dao: MovieDao
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _watchlistMovies = MutableStateFlow<List<WatchlistMovieEntity>>(emptyList())
    val watchlistMovies: StateFlow<List<WatchlistMovieEntity>> = _watchlistMovies

    fun refreshUpcoming() {
        _watchlistMovies.value = emptyList()
        dao.getAllFavorites()
    }

    init {
        viewModelScope.launch {
            dao.getAllWatchlist()
                .onStart {
                    _isLoading.value = true
                }
                .catch {
                    _isLoading.value = false
                    _watchlistMovies.value = emptyList()
                }
                .collect { movies ->
                    _watchlistMovies.value = movies
                    _isLoading.value = false
                }
        }
    }
}