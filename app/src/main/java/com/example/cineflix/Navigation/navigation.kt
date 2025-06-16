package com.example.cineflix.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cineflix.Screen.NetflixCreateAccountScreen
import com.example.cineflix.Screen.ReadyToWatchScreen


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "ReadyToWatchScreen") {
        composable("ReadyToWatchScreen") {
            ReadyToWatchScreen(navController)
        }
        composable("Signupscreen") {
            NetflixCreateAccountScreen(navController)
        }
    }
}