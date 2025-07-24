//package com.example.cineflix.Screen.Gamesscreen
//
//import androidx.annotation.DrawableRes
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowForwardIos
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import com.example.cineflix.R
//import com.example.cineflix.Retrofit.FreeToGame
//import com.example.cineflix.Retrofit.GamesViewModel
//import com.example.cineflix.Screen.Homescreen.Game
//import kotlin.collections.take
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun GameHomeScreen(
//    navController: NavController,
//    viewModel: GamesViewModel = viewModel(),
//) {
//    val shooterGames by viewModel.shooterGames.collectAsState()
//    val mmorpgGames by viewModel.mmorpgGames.collectAsState()
//    val pvpGames by viewModel.pvpGames.collectAsState()
//    val fantasyGames by viewModel.fantasyGames.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("FreeToGame", color = Color.White) },
//                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Black)
//            )
//        },
//        bottomBar = {
//            BottomAppBar(
//                containerColor = Color.Black,
//                contentColor = Color.White,
//                content = {
//                    IconButton(onClick = {  }) {
//                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
//                    }
//                    IconButton(onClick = { }) {
//                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
//                    }
//                }
//            )
//        },
//        containerColor = Color.Black
//    ) { padding ->
//        LazyColumn(
//            contentPadding = padding,
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Black)
//        ) {
//            item {
//                GameSection(title = "Shooter Games", games = shooterGames, navController)
//                GameSection(title = "MMORPG", games = mmorpgGames.shuffled(), navController)
//                GameSection(title = "PVP Games", games = pvpGames, navController)
//                GameSection(title = "Fantasy Picks", games = fantasyGames.shuffled(), navController)
//            }
//        }
//    }
//}
//@Composable
//fun GameSection(title: String, games: List<FreeToGame>, navController: NavController) {
//    Column(modifier = Modifier.padding(16.dp)) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 2.dp, vertical = 8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(title, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text("See All", color = Color.White)
//                Icon(Icons.Default.ArrowForwardIos, tint = Color.White, contentDescription = null)
//            }
//        }
//
//        LazyRow {
//            items(games.take(10)) { game ->
//                GameCard(game = game) {
//                    // navController.navigate("gameDetail/${game.id}")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun GameCard(game: FreeToGame, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .width(140.dp)
//            .padding(end = 12.dp)
//            .clickable { onClick() },
//        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            AsyncImage(
//                model = game.thumbnail,
//                contentDescription = game.title,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .height(100.dp)
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(8.dp))
//            )
//            Spacer(modifier = Modifier.height(6.dp))
//            Text(
//                text = game.title,
//                color = Color.White,
//                fontSize = 13.sp,
//                fontWeight = FontWeight.SemiBold,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//            Text(
//                text = game.genre,
//                color = Color.Gray,
//                fontSize = 11.sp
//            )
//        }
//    }
//}
