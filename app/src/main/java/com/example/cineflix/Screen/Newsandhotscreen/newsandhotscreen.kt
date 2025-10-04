package com.example.cineflix.Screen.Newsandhotscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.cineflix.Screen.Homescreen.BottomBar
import com.example.cineflix.Viewmodel.NetflixViewModel
import com.example.cineflix.ui.theme.CineflixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAndHotScreen(
    navController: NavHostController,
    viewModel: NetflixViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

) {
    val tabs = listOf("Coming Soon", "Everyone's Watching")
    var selectedTab by remember { mutableStateOf(tabs[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "New & Hot",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                    }
                    IconButton(onClick = {navController.navigate("search")}) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = {
            BottomBar(navController)
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black)
        ) {
            val emojis = listOf("🍿", "🔥")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, tab ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (selectedTab == tab) Color.Red else Color.DarkGray)
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${emojis[index]} $tab",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 🔥 Show different data depending on the selected tab
            val movieList = when (selectedTab) {
                "Coming Soon" -> viewModel.onlyOnNetflix
                "Everyone's Watching" -> viewModel.bollywood
                else -> emptyList()
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(movieList.size) { index ->
                    val movie = movieList[index]
                    ShowCard(
                        imageUrl = movie.Poster,
                        ageRating = movie.rating ?: "U/A",
                        title = movie.title,
                        description = movie.description ?: "No description available"
                    )
                }
            }
        }
    }
}



@Composable
fun ShowCard(imageUrl: String, ageRating: String, title: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, Color.DarkGray), RoundedCornerShape(8.dp))
            .background(Color.Black)
            .padding(8.dp)
    ) {
        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(bottomStart = 4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(ageRating, color = Color.White, fontSize = 12.sp)
            }
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { },
            modifier = Modifier.padding(horizontal = 8.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Black)
            Spacer(Modifier.width(8.dp))
            Text("Remind Me", color = Color.Black)
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CineflixTheme {
        NewAndHotScreen(rememberNavController())
    }
}