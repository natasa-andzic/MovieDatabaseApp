package com.natasaandzic.moviedatabase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.db.FavoriteMovieEntity
import com.natasaandzic.moviedatabase.db.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val dao: MovieDao
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _favoriteMovies = MutableStateFlow<List<FavoriteMovieEntity>>(emptyList())
    val favoriteMovies: StateFlow<List<FavoriteMovieEntity>> = _favoriteMovies

    init {
        loadFavorites()
    }

    fun refresh(){
        _favoriteMovies.value=emptyList()
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            dao.getAllFavorites()
                .onStart {
                    _isLoading.value = true
                }
                .catch {
                    _isLoading.value = false
                    _favoriteMovies.value = emptyList()
                }
                .collect { movies ->
                    _favoriteMovies.value = movies
                    _isLoading.value = false
                }
        }
    }
}

