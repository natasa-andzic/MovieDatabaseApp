package com.natasaandzic.moviedatabase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasaandzic.moviedatabase.db.MovieDao
import com.natasaandzic.moviedatabase.db.FavoriteMovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val dao: MovieDao
) : ViewModel() {

    val favoriteMovies: StateFlow<List<FavoriteMovieEntity>> =
        dao.getAllFavorites()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
