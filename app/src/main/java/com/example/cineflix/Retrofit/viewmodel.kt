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
    var hollywoodMovies by mutableStateOf<List<MovieResponse>>(emptyList())
    var comedyMovies by mutableStateOf<List<MovieResponse>>(emptyList())
    var fantasyMovies by mutableStateOf<List<MovieResponse>>(emptyList())
    var susTvShows by mutableStateOf<List<MovieResponse>>(emptyList())
    var selectedMovie by mutableStateOf<MovieResponse?>(null)
        private set
    private val apiKey = "f3542b87"

    init {
        fetchMovieList()
    }

    private fun fetchMovieList() {
        viewModelScope.launch {
            bollywood = fetchMovies(listOf("tt0995031", "tt26225118", "tt0499375","tt0248126","tt0367110","tt26733317","tt0346723",
                "tt21383812","tt5764024","tt21626284","tt26932223","tt23790740","tt10028196","tt0449994"))
          onlyOnNetflix = fetchMovies(listOf("tt10919420", "tt30003786", "tt4574334","tt10795658","tt28106741","tt6468322","tt6077448","tt3597912","tt2442560"))
           kDramas = fetchMovies(listOf("tt26471411", "tt14169960", "tt29569035","tt30423279","tt14819828","tt27668559","tt26693803","tt13274038",
               "tt11138512"))
            susTvShows = fetchMovies(listOf("tt0401916","tt0903747","tt19072562","tt10730376","tt8421350","tt31158589","tt9319668"))
            comedyMovies = fetchMovies(listOf("tt0242519","tt0805184","tt0995031","tt0488798","tt3159708","tt0464160","tt1573072","tt0807758","tt1464588"))
            fantasyMovies = fetchMovies(listOf("tt2338151","tt1270797","tt0110475","tt0113497","tt0451850","tt0948470"))
           // hollywoodMovies = fetchMovies(listOf("tt2078092","tt13457350","tt1663202","tt2821224","tt15313716","tt13054826","tt16419174","tt13225730"))
        }
    }

    fun fetchMovieById(imdbId: String) {
        viewModelScope.launch {
            try {
                val response = OmdbRetrofitInstance.api.getMovieById(imdbId, apiKey)
                if (response.Response == "True") {
                    selectedMovie = response
                    Log.d("NetflixViewModel", "Loaded movie: ${response.title}")
                } else {
                    Log.e("NetflixViewModel", "Movie not found: $imdbId")
                }
            } catch (e: Exception) {
                Log.e("NetflixViewModel", "Error loading $imdbId: ${e.message}")
            }
        }
    }
    private suspend fun fetchMovies(titles: List<String>): List<MovieResponse> {
        return titles.mapNotNull { title ->
            try {
                val response = OmdbRetrofitInstance.api.getMovieById(title, apiKey)
                if (response.Response == "True") {
                    Log.d("NetflixView", "Loaded movie: ${response.title}")
                    response
                } else {
                    Log.e("NetflixView", "Movie not found: $title")
                    null
                }
            } catch (e: Exception) {
                Log.e("NetflixView", "Error loading $title: ${e.message}")
                null
            }
        }
    }

}
