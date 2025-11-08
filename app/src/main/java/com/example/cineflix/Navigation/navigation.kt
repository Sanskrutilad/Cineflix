package com.example.cineflix.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cineflix.AI.ChatScreen
import com.example.cineflix.Retrofit.ApiService
import com.example.cineflix.Retrofit.ProfileViewModel
import com.example.cineflix.Screen.AddProfileScreen
import com.example.cineflix.Screen.Gamesscreen.GameHomeScreen
import com.example.cineflix.Screen.Gamesscreen.GameSearchScreen
import com.example.cineflix.Screen.HelpScreen
import com.example.cineflix.Screen.Homescreen.Castdetailsscreen
import com.example.cineflix.Screen.Homescreen.CategoryScreen
import com.example.cineflix.Screen.Homescreen.MovieDetailScreen
import com.example.cineflix.Screen.Homescreen.MovieListScreen
import com.example.cineflix.Screen.Homescreen.MoviesScreen
import com.example.cineflix.Screen.Homescreen.MyListScreen
import com.example.cineflix.Screen.Homescreen.NetflixTopBarScreen
import com.example.cineflix.Screen.Homescreen.SearchScreen
import com.example.cineflix.Screen.Homescreen.TvShowScreen
import com.example.cineflix.Screen.Homescreen.sampleCategories
import com.example.cineflix.Screen.NetflixCreateAccountScreen
import com.example.cineflix.Screen.NetflixLoginScreen
import com.example.cineflix.Screen.NetflixSimpleWelcomeScreen
import com.example.cineflix.Screen.Newsandhotscreen.NewAndHotScreen
import com.example.cineflix.Screen.ReadyToWatchScreen
import com.example.cineflix.Screen.SplashScreen
import com.example.cineflix.Screen.WhosWatchingScreen
import com.example.cineflix.Screen.settingscreen.AppSettingsScreen
import com.example.cineflix.Screen.settingscreen.Settingmainscreen


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Navigation(apiService: ApiService) {
    val navController = rememberNavController()
    val profileViewModel: ProfileViewModel = viewModel()
    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("Splashscreen"){
            SplashScreen(navController)
        }
        composable("settingscreen"){
            Settingmainscreen(apiService,navController)
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
            AddProfileScreen(navController,apiService,profileViewModel)
        }
        composable("WhosWatchingScreen") {
            WhosWatchingScreen(navController,profileViewModel)
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
            CategoryScreen(
                categories = sampleCategories,
                navController = navController
            )
        }
        composable("movies/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            MovieListScreen(category,  navController)
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
        composable("NewsandHot") {
            NewAndHotScreen(
                navController = navController,
            )
        }
        composable("search") {
            SearchScreen(
                navController,
            )
        }
        composable("mylist") {
            MyListScreen(navController = navController)
        }
        composable("gamesearch") {
            GameSearchScreen(navController)
        }
        composable("aichat") {
            ChatScreen()
        }


    }
}
