package com.example.cineflix.Screen.Homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cineflix.Retrofit.MovieResponse
import com.example.cineflix.Viewmodel.NetflixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    category: String,
    navController: NavHostController
) {
    val viewModel: NetflixViewModel = viewModel()

    // Access allMovies from ViewModel
    val allMovies = viewModel.allMovies

    // Filter based on category or language
    val filteredMovies = remember(category, allMovies) {
        allMovies.filter { movie ->
            movie.Genre.any { it.equals(category, ignoreCase = true) } ||
                    movie.languages.any { it.equals(category, ignoreCase = true) }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = category, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        if (filteredMovies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No movies found in $category", color = Color.Gray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // Netflix-style 3 posters per row
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredMovies) { movie ->
                    MoviePosterItem(movie = movie , navController)
                }
            }
        }
    }
}

@Composable
fun MoviePosterItem(movie: MovieResponse , navController : NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f) // Poster aspect ratio
            .clickable {
                navController.navigate("movieDetail/${movie.Imdbid}")
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = movie.Poster),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
                .background(Color.DarkGray)
        )
    }
}
