package com.example.cineflix.Viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineflix.Retrofit.FreeToGame
import com.example.cineflix.Retrofit.FreeToGameRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GamesViewModel : ViewModel() {

    private val _shooterGames = MutableStateFlow<List<FreeToGame>>(emptyList())
    val shooterGames: StateFlow<List<FreeToGame>> = _shooterGames

    private val _mmorpgGames = MutableStateFlow<List<FreeToGame>>(emptyList())
    val mmorpgGames: StateFlow<List<FreeToGame>> = _mmorpgGames

    private val _pvpGames = MutableStateFlow<List<FreeToGame>>(emptyList())
    val pvpGames: StateFlow<List<FreeToGame>> = _pvpGames

    private val _fantasyGames = MutableStateFlow<List<FreeToGame>>(emptyList())
    val fantasyGames: StateFlow<List<FreeToGame>> = _fantasyGames
    val allGames: List<FreeToGame>
        get() = pvpGames.value + mmorpgGames.value + fantasyGames.value + shooterGames.value

    init {
        fetchGamesByCategory("shooter", _shooterGames)
        fetchGamesByCategory("mmorpg", _mmorpgGames)
        fetchGamesByCategory("pvp", _pvpGames)
        fetchGamesByCategory("fantasy", _fantasyGames)
    }

    private fun fetchGamesByCategory(category: String, stateFlow: MutableStateFlow<List<FreeToGame>>) {
        viewModelScope.launch {
            try {
                val result = FreeToGameRetrofit.api.getGames(category = category)
                stateFlow.value = result
            } catch (e: Exception) {
                Log.e("GamesViewModel", "Error fetching $category games: ${e.message}")
            }
        }
    }
}
