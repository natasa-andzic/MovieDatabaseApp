package com.natasaandzic.moviedatabase.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.natasaandzic.moviedatabase.viewmodel.SearchViewModel
import androidx.compose.runtime.getValue

@Composable
fun SearchScreen(viewModel: SearchViewModel = hiltViewModel(), onMovieClicked: (Int) -> Unit) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextField(
            value = query,
            onValueChange = viewModel::onQueryChanged,
            placeholder = { Text("Search for movies...") },
            modifier = Modifier.fillMaxWidth()
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(results) { movie ->
                Text(
                    text = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onMovieClicked(movie.id) }
                )
            }
        }
    }
}
