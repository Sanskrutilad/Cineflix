package com.example.cineflix.Retrofit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NetflixViewModel : ViewModel() {
    var bollywood by mutableStateOf<List<MovieResponse>>(emptyList())
    var onlyOnNetflix by mutableStateOf<List<MovieResponse>>(emptyList())
    var kDramas by mutableStateOf<List<MovieResponse>>(emptyList())

    private val apiKey = "f3542b87"

    init {
        fetchMovieList()
    }

    private fun fetchMovieList() {
        viewModelScope.launch {
            bollywood = fetchMovies(listOf("Guardians of the Galaxy Vol. 2", "Guardians of the Galaxy Vol. 2", "Guardians of the Galaxy Vol. 2"))
            onlyOnNetflix = fetchMovies(listOf("Guardians of the Galaxy Vol. 2", "Guardians of the Galaxy Vol. 2", "Guardians of the Galaxy Vol. 2"))
            kDramas = fetchMovies(listOf("Guardians of the Galaxy Vol. 2", "Guardians of the Galaxy Vol. 2", "Guardians of the Galaxy Vol. 2"))
        }
    }

    private suspend fun fetchMovies(titles: List<String>): List<MovieResponse> {
        return titles.mapNotNull { title ->
            try {
                val response = OmdbRetrofitInstance.api.getMovieByTitle(title, apiKey)
                if (response.Response == "True") {
                    Log.d("NetflixViewModel", "Loaded movie: ${response.Title}")
                    response
                } else {
                    Log.e("NetflixViewModel", "Movie not found: $title")
                    null
                }
            } catch (e: Exception) {
                Log.e("NetflixViewModel", "Error loading $title: ${e.message}")
                null
            }
        }
    }

}
