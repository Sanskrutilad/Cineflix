package com.example.cineflix.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cineflix.Retrofit.ApiService
import com.example.cineflix.Screen.AddProfileScreen
import com.example.cineflix.Screen.NetflixCreateAccountScreen
import com.example.cineflix.Screen.ReadyToWatchScreen


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Navigation(apiService: ApiService) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "whoswatching") {
        composable("ReadyToWatchScreen") {
            ReadyToWatchScreen(navController)
        }
        composable("Signupscreen") {
            NetflixCreateAccountScreen(navController)
        }
        composable("whoswatching") {
            AddProfileScreen(navController,apiService)
        }
    }
}