package com.example.cineflix.Screen.Homescreen

import android.content.res.Resources
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cineflix.R // update with your actual package
import com.example.cineflix.Retrofit.MovieResponse
import com.example.cineflix.Retrofit.NetflixViewModel

@Composable
fun NetflixTopBarScreen() {
    val scrollState = rememberLazyListState()

    val showChips by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset < 20
        }
    }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            BottomBar(selected = "Home")
        }
    ) { paddingValues ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier.padding(paddingValues)
        ) {
            item { TopAppBarContent() }

            item {
                AnimatedVisibility(
                    visible = showChips,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    FilterChipsRow()
                }
            }

            item { FeaturedBanner() }

            item { MobileGamesSection() }

            item {
                NetflixHomeScreen()
            }
        }

    }
}

@Composable
fun TopAppBarContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = R.drawable.netlogo,
            contentDescription = "Netflix Logo",
            modifier = Modifier.size(86.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(Icons.Default.Download, contentDescription ="Downloads",tint = Color.White,modifier=Modifier.size(35.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White,modifier=Modifier.size(35.dp))
    }
}

@Composable
fun FilterChipsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilterChip("TV Shows")
        FilterChip("Movies")
        FilterChip("Categories", showDropdown = true)
    }
}

@Composable
fun FilterChip(text: String, showDropdown: Boolean = false) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = Color.Black)
        if (showDropdown) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
        }
    }
}

@Composable
fun FeaturedBanner() {
    Column(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{ }
                .padding(15.dp)
                .height(500.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.DarkGray),
            contentAlignment = Alignment.BottomCenter
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RectangleShape,
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f)
                        .padding(start=10.dp,end=5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.Black,
                        modifier = Modifier.size(35.dp)
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    Text("Play", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 23.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = {  },
                    shape = RectangleShape,
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f)
                        .padding(start=5.dp,end=10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "+My List",
                        tint = Color.White,
                        modifier = Modifier.size(35.dp)
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    Text("My List", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun MobileGamesSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mobile Games", color = Color.White, fontWeight = FontWeight.Bold)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("See All", color = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForwardIos, tint = Color.White, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow {
            items(5) { index ->
                Card(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(end = 8.dp)
                        .clickable {

                        },
                    colors = CardDefaults.cardColors(containerColor = Color.Gray),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("Game ${index + 1}", color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(selected: String = "Home") {
    val items = listOf("Home", "Games", "New & Hot", "My Netflix")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.VideogameAsset,
        Icons.Default.Whatshot,
        Icons.Default.Person
    )
    BottomAppBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items.forEachIndexed { index, item ->
                Column(
                    modifier = Modifier
                        .clickable {  }
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (item == "My Netflix") {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1A73E8)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = item,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = item,
                            tint = if (item == selected) Color.White else Color.Gray
                        )
                    }
                    Text(
                        text = item,
                        fontSize = 10.sp,
                        color = if (item == selected) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: MovieResponse,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(140.dp)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = movie.Poster,
            contentDescription = movie.Title,
            modifier = Modifier
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

    }
}

@Composable
fun MovieSection(title: String, movies: List<MovieResponse>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
        )
        LazyRow {
            items(movies) { movie ->
                MovieCard(movie = movie)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun NetflixHomeScreen() {
    val movieviewmodel: NetflixViewModel = viewModel()
    val bollywood = movieviewmodel.bollywood
    val onlyOnNetflix = movieviewmodel.onlyOnNetflix
    val kDramas = movieviewmodel.kDramas

    Column {
        MovieSection(title = "Bollywood Movies", movies = bollywood)
        MovieSection(title = "Top 10 Mobile Games", movies = kDramas.shuffled())
        MovieSection(title = "Only on Netflix", movies = onlyOnNetflix)
        MovieSection(title = "K-Dramas", movies = kDramas)
        MovieSection(title = "New on Netflix", movies = kDramas.shuffled())
        MovieSection(title = "Comedy Movies", movies = kDramas.shuffled())
        MovieSection(title = "Only on Netflix", movies = kDramas.shuffled())
        MovieSection(title = "NX: Super-Powered Sci-Fi, Fantasy& More", movies = kDramas.shuffled())
        MovieSection(title = "Top 10 Movies in India Today", movies = kDramas.shuffled())
        MovieSection(title = "Hollywood Movies", movies = kDramas.shuffled())
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NetflixTopBarPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NetflixTopBarScreen()
    }
}