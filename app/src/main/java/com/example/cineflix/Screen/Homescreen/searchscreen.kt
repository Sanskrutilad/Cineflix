package com.example.cineflix.Screen.Homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.example.cineflix.Viewmodel.NetflixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(

    navController: NavController,
    viewModel: NetflixViewModel = viewModel()
) {
    val searchQuery = viewModel.searchQuery
    val searchResults = viewModel.searchResults

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = {
                    Text(
                        "Search for movies, shows...",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp)), // Rounded pill style
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    cursorColor = Color.Red,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }

        // 🎯 Search Results / Top Searches
        if (searchQuery.isNotBlank()) {
            Text(
                text = "Search Results",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            LazyColumn {
                items(searchResults) { movie ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = movie.Poster,
                            contentDescription = movie.title,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = movie.title ?: "Untitled",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Top Searches",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            val topSearches = (viewModel.bollywood + viewModel.onlyOnNetflix + viewModel.kDramas + viewModel.susTvShows).take(5)

            LazyColumn {
                items(topSearches) { movie ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = movie.Poster,
                            contentDescription = movie.title,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = movie.title ?: "Untitled",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.fetchTrailerByImdbId(movie.Imdbid) }) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
