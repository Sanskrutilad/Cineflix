package com.example.cineflix.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cineflix.Retrofit.ApiService
import com.example.cineflix.Screen.AddProfileScreen
import com.example.cineflix.Screen.HelpScreen
import com.example.cineflix.Screen.Homescreen.MovieDetailScreen
import com.example.cineflix.Screen.Homescreen.NetflixTopBarScreen
import com.example.cineflix.Screen.NetflixCreateAccountScreen
import com.example.cineflix.Screen.NetflixLoginScreen
import com.example.cineflix.Screen.ReadyToWatchScreen
import com.example.cineflix.Screen.SplashScreen


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Navigation(apiService: ApiService) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("Splashscreen") {
            SplashScreen(navController)
        }
        composable("ReadyToWatchScreen") {
            ReadyToWatchScreen(navController)
        }
        composable("Signupscreen") {
            NetflixCreateAccountScreen(navController)
        }
        composable("Helpscreen") {
            HelpScreen(navController)
        }

        composable("loginscreen") {
            NetflixLoginScreen(navController,apiService)
        }
        composable("whoswatching") {
            AddProfileScreen(navController,apiService)
        }
        composable("HomeScreen") {
            NetflixTopBarScreen(navController)
        }
        composable("MoviedetailScreen/{Imbdid}") {backStackEntry->
            val Imbdid = backStackEntry.arguments?.getString("Imbdid") ?: ""
            MovieDetailScreen(navController,Imbdid)
        }
    }
}