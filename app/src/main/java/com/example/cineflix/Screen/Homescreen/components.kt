package com.example.cineflix.Screen.Homescreen

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.cineflix.R // update with your actual package
import com.example.cineflix.Retrofit.ApiService
import com.example.cineflix.Retrofit.MovieResponse
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Games
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.cineflix.Screen.generateProfileId
import com.example.cineflix.Viewmodel.GamesViewModel
import com.example.cineflix.Viewmodel.MyListMovie
import com.example.cineflix.Viewmodel.MyListViewModel

fun extractDominantColorFromUrl(
    context: Context,
    imageUrl: String,
    onColorExtracted: (Color) -> Unit
) {
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .allowHardware(false)
        .target { drawable ->
            val bitmap = (drawable as BitmapDrawable).bitmap
            Palette.from(bitmap).generate { palette ->
                val dominantColorInt = palette?.getDominantColor(Color.DarkGray.toArgb())
                val color = dominantColorInt?.let { Color(it) } ?: Color.DarkGray
                onColorExtracted(color)
            }
        }
        .build()
    val imageLoader = ImageLoader(context)
    imageLoader.enqueue(request)
}

@Composable
fun TopAppBarContent(backgroundColor1: Color , navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor1.copy(alpha = 0.85f)) // slightly transparent
            .padding(horizontal = 16.dp, vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = R.drawable.netlogo,
            contentDescription = "Netflix Logo",
            modifier = Modifier.size(86.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Download,
            contentDescription = "Downloads",
            tint = Color.White,
            modifier = Modifier.size(35.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier.size(35.dp).clickable{navController.navigate("search")}
        )
    }
}

@Composable
fun FilterChipsRow(backgroundColor: Color, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        FilterChip("TV Shows", route = "TVShows", navController = navController)
        FilterChip("Movies", route = "movies", navController = navController)
        FilterChip("Categories", showDropdown = true, route = "CategoryScreen", navController = navController)
    }
}

@Composable
fun FilterChip(
    text: String,
    showDropdown: Boolean = false,
    route: String,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.5f),
                shape = RoundedCornerShape(50)
            )
            .background(Color.Transparent)
            .clickable { navController.navigate(route) }
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = Color.White)
        if (showDropdown) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun FeaturedBanner(
    navController: NavHostController,
    backgroundColor: Color,
    featuredMovie: MovieResponse,
    myListViewModel: MyListViewModel = viewModel()
) {
    val context = LocalContext.current
    val isInList = remember { mutableStateOf(false) }
    val profileId = remember { generateProfileId() }

    LaunchedEffect(featuredMovie.Imdbid) {
        featuredMovie.Imdbid?.let { id ->
            myListViewModel.isMovieInMyList(id) { exists ->
                isInList.value = exists
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    featuredMovie.Imdbid?.let { id ->
                        navController.navigate("MoviedetailScreen/$id")
                    }
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            // 🎬 Poster
            Crossfade(targetState = featuredMovie.Poster, label = "posterFade") { posterUrl ->
                AsyncImage(
                    model = posterUrl,
                    contentDescription = featuredMovie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = {
                        featuredMovie.Imdbid?.let { id ->
                            navController.navigate("MoviedetailScreen/$id")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RectangleShape,
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f)
                        .padding(start = 10.dp, end = 5.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.Black)
                    Spacer(Modifier.width(4.dp))
                    Text("Play", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Spacer(Modifier.width(12.dp))


                OutlinedButton(
                    onClick = {
                        featuredMovie.Imdbid?.let { id ->
                            if (isInList.value) {
                                // Remove
                                myListViewModel.removeMovieFromMyList(profileId,id) { success ->
                                    if (success) {
                                        isInList.value = false
                                        Log.d("FeaturedBanner", "Movie removed ❌")
                                    }
                                }
                            } else {
                                // Add
                                val movie = MyListMovie(
                                    imdbId = id,
                                    title = featuredMovie.title ?: "",
                                    poster = featuredMovie.Poster ?: ""
                                )
                                myListViewModel.addMovieToMyList(profileId,movie) { success ->
                                    if (success) {
                                        isInList.value = true
                                        Log.d("FeaturedBanner", "Movie added ✅")
                                    }
                                }
                            }
                        }
                    },
                    shape = RectangleShape,
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f)
                        .padding(start = 5.dp, end = 10.dp)
                ) {
                    if (isInList.value) {
                        Icon(Icons.Default.Check, contentDescription = "In My List", tint = Color.White)
                        Spacer(Modifier.width(4.dp))
                        Text("Added", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    } else {
                        Icon(Icons.Default.Add, contentDescription = "+My List", tint = Color.White)
                        Spacer(Modifier.width(4.dp))
                        Text("My List", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}



@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

@Composable
fun MobileGamesSection(
    backgroundColor: Color,
    navController: NavHostController,
    viewModel: GamesViewModel = viewModel(),
) {
    val shooterGames by viewModel.shooterGames.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mobile Games", color = Color.White, fontWeight = FontWeight.Bold,  style = MaterialTheme.typography.titleLarge,)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("See All", color = Color.White, modifier = Modifier.clickable{navController.navigate("GameHomeScreen")})
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForwardIos, tint = Color.White, contentDescription = null, modifier = Modifier.clickable{navController.navigate("GameHomeScreen")})
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (shooterGames.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            LazyRow {
                items(shooterGames.take(10)) { game ->
                    Card(
                        modifier = Modifier
                            .width(140.dp)
                            .padding(end = 12.dp)
                            .clickable {
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Box(modifier = Modifier.height(100.dp)) {
                                AsyncImage(
                                    model = game.thumbnail,
                                    contentDescription = game.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.DarkGray)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = game.title,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = game.genre,
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

data class Game(
    val imageUrl: String,
    val title: String,
    val genre: String
)

@Composable
fun BottomBar(navController: NavHostController, selected: String = "HomeScreen") {
    val items = listOf("HomeScreen", "NewsandHot","GameHomeScreen", "settingscreen")
    val labels = listOf("Home", "New & Hot", "Games", "My Netflix")
    val icons = listOf(Icons.Default.Home, Icons.Default.Whatshot, Icons.Default.Games, Icons.Default.Person)

    BottomAppBar(containerColor = Color.Black, contentColor = Color.White) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp).padding(start = 15.dp,end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                BottomBarItem(
                    label = labels[index],
                    icon = icons[index],
                    isSelected = selected == item,
                    onClick = { navController.navigate(item) }
                )
            }
        }
    }
}


@Composable
fun BottomBarItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (label == "My Netflix") {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF1A73E8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        else {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isSelected) Color.White else Color.Gray
        )
    }
}

@Composable
fun MovieCard(
    movie: MovieResponse,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .clickable {navController.navigate("MoviedetailScreen/${movie.Imdbid}")  }
            .width(140.dp)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = movie.Poster,
            contentDescription = movie.title,
            modifier = Modifier
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun MovieSection(title: String, movies: List<MovieResponse>,navController: NavHostController) {
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
                MovieCard(movie = movie,navController)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}




