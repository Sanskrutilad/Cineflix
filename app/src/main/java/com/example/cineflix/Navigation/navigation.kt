package com.example.cineflix.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cineflix.Retrofit.ApiService
import com.example.cineflix.Screen.AddProfileScreen
import com.example.cineflix.Screen.Gamesscreen.GameHomeScreen
import com.example.cineflix.Screen.HelpScreen
import com.example.cineflix.Screen.Homescreen.Castdetailsscreen
import com.example.cineflix.Screen.Homescreen.MovieDetailScreen
import com.example.cineflix.Screen.Homescreen.MoviesScreen
import com.example.cineflix.Screen.Homescreen.NetflixTopBarScreen
import com.example.cineflix.Screen.Homescreen.PreviewCategoryScreen
import com.example.cineflix.Screen.Homescreen.TvShowScreen
import com.example.cineflix.Screen.NetflixCreateAccountScreen
import com.example.cineflix.Screen.NetflixLoginScreen
import com.example.cineflix.Screen.NetflixSimpleWelcomeScreen
import com.example.cineflix.Screen.ReadyToWatchScreen
import com.example.cineflix.Screen.SplashScreen
import com.example.cineflix.Screen.WhosWatchingScreen
import com.example.cineflix.Screen.settingscreen.AppSettingsScreen
import com.example.cineflix.Screen.settingscreen.Settingmainscreen


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Navigation(apiService: ApiService) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Splashscreen") {
        composable("Splashscreen"){
            SplashScreen(navController)
        }
        composable("settingscreen"){
            Settingmainscreen(navController)
        }
        composable("ReadyToWatchScreen"){
            ReadyToWatchScreen(navController)
        }
        composable("Signupscreen"){
            NetflixCreateAccountScreen(navController)
        }
        composable("appsettings"){
            AppSettingsScreen(navController)
        }
        composable("Helpscreen") {
            HelpScreen(navController)
        }
        composable("loginscreen") {
            NetflixLoginScreen(navController)
        }
        composable("AddProfileScreen") {
            AddProfileScreen(navController,apiService)
        }
        composable("WhosWatchingScreen") {
            WhosWatchingScreen(navController)
        }
        composable("HomeScreen") {
            NetflixTopBarScreen(navController)
        }
        composable("Menuscreen") {
            NetflixTopBarScreen(navController)
        }
        composable("GameHomeScreen") {
            GameHomeScreen(navController)
        }
        composable("CategoryScreen") {
            PreviewCategoryScreen(navController)
        }
        composable("castdetailscreen/{Imbdid}") {backStackEntry->
            val Imbdid = backStackEntry.arguments?.getString("Imbdid") ?: ""
            Castdetailsscreen(Imbdid, navController)
        }
        composable("MoviedetailScreen/{Imbdid}") {backStackEntry->
            val Imbdid = backStackEntry.arguments?.getString("Imbdid") ?: ""
            MovieDetailScreen(navController,Imbdid,apiService)
        }
        composable("NetflixSimpleWelcomeScreen") {
            NetflixSimpleWelcomeScreen(navController)
        }
        composable("movies") {
            MoviesScreen(navController = navController)
        }
        composable("TVShows") {
            TvShowScreen(navController = navController)
        }

    }
}