package com.example.cineflix.Screen.Gamesscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cineflix.Retrofit.FreeToGame
import com.example.cineflix.Retrofit.MovieResponse
import com.example.cineflix.Viewmodel.GamesViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSearchScreen(
    navController: NavController,
    viewModel: GamesViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    val allGames = viewModel.allGames
    val filteredGames = remember(searchQuery, allGames) {
        if (searchQuery.isBlank()) emptyList()
        else allGames.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 🔍 Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "Search for games...",
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        modifier = Modifier.height(45.dp)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp)),
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
                        IconButton(onClick = { searchQuery = "" }) {
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

        // 🕹 Search Results or Top Picks
        if (searchQuery.isNotBlank()) {
            Text(
                text = "Search Results",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            if (filteredGames.isEmpty()) {
                Text(
                    text = "No games found.",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn {
                    items(filteredGames) { game ->
                        GameSearchItem(game)
                    }
                }
            }
        } else {
            Text(
                text = "Top Picks",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            val topGames = (viewModel.shooterGames.value +
                    viewModel.mmorpgGames.value +
                    viewModel.pvpGames.value +
                    viewModel.fantasyGames.value)
                .shuffled()
                .take(5)

            LazyColumn {
                items(topGames) { game ->
                    GameSearchItem(game)
                }
            }
        }
    }
}

@Composable
fun GameSearchItem(game: FreeToGame) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = game.thumbnail,
            contentDescription = game.title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = game.title,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
