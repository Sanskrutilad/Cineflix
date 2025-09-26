package com.example.cineflix.Screen.Homescreen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.cineflix.Retrofit.MovieResponse
import com.example.cineflix.Viewmodel.NetflixViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TvShowScreen(
    navController: NavHostController,
    netflixViewModel: NetflixViewModel = viewModel()
) {
    val context = LocalContext.current
    //val onlyOnNetflix = netflixViewModel.onlyOnNetflix
    val kDramas = netflixViewModel.kDramas
    if (kDramas.isEmpty()) {
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
    val featuredMovie = kDramas.first()
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
                    val kDramas = movieviewmodel.kDramas
                    val susTvShows = movieviewmodel.susTvShows
                    val fantasyMovies = movieviewmodel.fantasyMovies
                    Column {
                        MovieSection(title = "kDramas", movies = kDramas,navController)
                        MovieSection(title = "TV Shows", movies = susTvShows,navController)
                        //MovieSection(title = "Hollywood Movies", movies = hollywoodMovies)
                    }
                }
            }

            // Top bar overlay — now always on top & intercepts touches
            TvshowTopBar(
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
fun TvshowTopBar(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    TopAppBar(
        title = { Text("TV Shows", color = Color.White) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = {  }) {
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




