package com.example.cineflix.Screen.Homescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cineflix.Viewmodel.NetflixViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoviesScreen(
    navController: NavHostController,
    netflixViewModel: NetflixViewModel = viewModel()
) {
    val context = LocalContext.current
    //val onlyOnNetflix = netflixViewModel.onlyOnNetflix
    val bollywood = netflixViewModel.bollywood
    val comedy = netflixViewModel.comedyMovies
    val fantasyMovies = netflixViewModel.fantasyMovies
    val HollywoodMovies = netflixViewModel.hollywoodMovies
    if (bollywood.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Red)
        }
        return
    }

    var backgroundColor by remember { mutableStateOf(Color.DarkGray) }
    val featuredMovie = bollywood.first()

    LaunchedEffect(featuredMovie.Poster) {
        extractDominantColorFromUrl(context, featuredMovie.Poster) { c ->
            backgroundColor = c
        }
    }

    val scrollState = rememberLazyListState()

    Scaffold(
        containerColor = Color.Black,
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Main scrollable content
            LazyColumn(
                state = scrollState,
                modifier = Modifier.padding(paddingValues)
            ) {
                item { Spacer(modifier = Modifier.height(56.dp)) } // space for TopBar

                item {
                    CategoryChipsRow(backgroundColor)
                }

                // Inside LazyColumn in MoviesScreen
                item {
                    FeaturedBanner(
                        navController = navController,
                        backgroundColor = backgroundColor,
                        featuredMovie = featuredMovie
                    )
                }
                item {
                    val movieviewmodel: NetflixViewModel = viewModel()
                    val bollywood = movieviewmodel.bollywood
                    val comedyMovies = movieviewmodel.comedyMovies
                    val fantasyMovies = movieviewmodel.fantasyMovies
                    Column {
                        MovieSection(title = "Bollywood Movies", movies = bollywood,navController)

                        MovieSection(title = "Comedy Movies", movies = comedyMovies,navController)
                        MovieSection(title = "NX: Super-Powered Sci-Fi, Fantasy& More", movies = fantasyMovies,navController)
                        //MovieSection(title = "Hollywood Movies", movies = hollywoodMovies)
                    }
                }
            }

            // Top bar overlay — now always on top & intercepts touches
            MoviesTopBar(
                backgroundColor = backgroundColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesTopBar(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    TopAppBar(
        title = { Text("Component", color = Color.White) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = { /* Download */ }) {
                Icon(Icons.Default.FileDownload, contentDescription = null, tint = Color.White)
            }
            IconButton(onClick = { /* Search */ }) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor),
        modifier = modifier
    )
}


@Composable
fun CategoryChipsRow(backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(selected = true, onClick = {}, label = { Text("Movies") })
        FilterChip(selected = false, onClick = {}, label = { Text("All Categories") })
    }
}


