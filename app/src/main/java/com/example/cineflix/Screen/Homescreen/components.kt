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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.cineflix.Retrofit.NetflixViewModel

@Composable
fun NetflixTopBarScreen(navController: NavHostController) {
    val scrollState = rememberLazyListState()
    val showChips by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset < 20
        }
    }
    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            BottomBar(navController)
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
                NetflixHomeScreen(navController)
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
fun Castdetailsscreen(
    movieId: String,
    navController: NavController,
    viewModel: NetflixViewModel = viewModel()
) {
    val movie = viewModel.selectedMovie
    LaunchedEffect(movieId) {
        viewModel.fetchMovieById(movieId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        if (movie == null) {
            // Show loading or placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Red)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF242426))
                    .verticalScroll(rememberScrollState())
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title & Close Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = movie.title,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                SectionHeader(title = "Cast")
                Spacer(modifier = Modifier.height(16.dp))
                movie.cast.forEach { cast ->
                    Text(
                        text = cast,
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(21.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))
                SectionHeader(title = "Director")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = movie.director,
                    fontSize = 20.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                SectionHeader(title = "Writer")
                Spacer(modifier = Modifier.height(16.dp))
                movie.Writer.forEach { Writer ->
                    Text(
                        text = Writer,
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(21.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))
                SectionHeader(title = "Maturity Rating")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    movie.rating,
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .background(Color.DarkGray, shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                SectionHeader(title = "Genre")
                Spacer(modifier = Modifier.height(16.dp))
                movie.Genre.forEach { Genre ->
                    Text(
                        text = Genre,
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(21.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("See All", color = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForwardIos, tint = Color.White, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        val gameList = listOf(
            Game("https://i.imgur.com/WJ6mPci.png", "Squid Game: Unleashed", "Action"),
            Game("https://i.imgur.com/WJ6mPci.png", "Stranger Things 3", "Adventure"),
            Game("https://i.imgur.com/WJ6mPci.png", "Hextech Mayhem", "Music"),
            Game("https://i.imgur.com/WJ6mPci.png", "Twelve Minutes", "Thriller"),
            Game("https://i.imgur.com/WJ6mPci.png", "Before Your Eyes", "Narrative")
        )
        LazyRow {
            items(gameList) { game ->
                Card(
                    modifier = Modifier
                        .width(140.dp)
                        .padding(end = 12.dp)
                        .clickable {},
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Box(modifier = Modifier.height(100.dp)) {
                            AsyncImage(
                                model = game.imageUrl,
                                contentDescription = game.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.DarkGray)
                            )
                            Text(
                                text = "New Update",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .background(Color.Red, shape = RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                    .offset(x = 6.dp, y = 6.dp)
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

data class Game(
    val imageUrl: String,
    val title: String,
    val genre: String
)

@Composable
fun BottomBar(navController: NavHostController, selected: String = "HomeScreen") {
    val items = listOf("HomeScreen","Routes.NEW_HOT", "settingscreen")
    val labels = listOf("Home", "New & Hot", "My Netflix")
    val icons = listOf(Icons.Default.Home, Icons.Default.Whatshot, Icons.Default.Person)

    BottomAppBar(containerColor = Color.Black, contentColor = Color.White) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 46.dp, vertical = 6.dp)
        ) {
            // Home
            BottomBarItem(
                label = labels[0],
                icon = icons[0],
                isSelected = selected == items[0],
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { navController.navigate(items[0]) }
            )

            // New & Hot
            BottomBarItem(
                label = labels[1],
                icon = icons[1],
                isSelected = selected == items[1],
                modifier = Modifier.align(Alignment.Center),
                onClick = { navController.navigate(items[1]) }
            )

            // My Netflix
            BottomBarItem(
                label = labels[2],
                icon = icons[2],
                isSelected = selected == items[2],
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = { navController.navigate(items[2]) }
            )
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
        } else {
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

@Composable
fun NetflixHomeScreen(navController: NavHostController) {
    val movieviewmodel: NetflixViewModel = viewModel()
    val bollywood = movieviewmodel.bollywood
    val onlyOnNetflix = movieviewmodel.onlyOnNetflix
    val kDramas = movieviewmodel.kDramas
    val comedyMovies = movieviewmodel.comedyMovies
    val fantasyMovies = movieviewmodel.fantasyMovies
    val susTvShows = movieviewmodel.susTvShows
    val hollywoodMovies = movieviewmodel.hollywoodMovies
    Column {
        MovieSection(title = "Bollywood Movies", movies = bollywood,navController)
        MovieSection(title = "Top 10 Mobile Games", movies = kDramas.shuffled(),navController)
        MovieSection(title = "Top 10 Movies on Netflix", movies = kDramas.shuffled(),navController)
        MovieSection(title = "Only on Netflix", movies = onlyOnNetflix,navController)
        MovieSection(title = "K-Dramas", movies = kDramas,navController)
        MovieSection(title = "New on Netflix", movies = onlyOnNetflix.shuffled(),navController )
        MovieSection(title = "Comedy Movies", movies = comedyMovies,navController)
        MovieSection(title = "NX: Super-Powered Sci-Fi, Fantasy& More", movies = fantasyMovies,navController)
        //MovieSection(title = "Hollywood Movies", movies = hollywoodMovies)
        MovieSection(title = "Suspenseful Tv Shows", movies = susTvShows,navController)
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    navController: NavHostController,
    Imdb: String,
    apiService: ApiService,
    viewModel: NetflixViewModel = viewModel()
    ) {
    val scrollState = rememberScrollState()
    val movie = viewModel.selectedMovie
    val trailerId = viewModel.trailerId

    // Fetch movie by ID
    LaunchedEffect(Imdb) {
        viewModel.fetchMovieById(Imdb)
    }

    // Fetch trailer after movie is loaded
    LaunchedEffect(movie?.Imdbid) {
        if (!movie?.Imdbid.isNullOrEmpty()) {
            viewModel.fetchTrailerByImdbId(movie.Imdbid)
        }
    }


    if (movie == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Red)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.Black)
            .padding(bottom = 60.dp)
    ) {
        // Thumbnail and header icons
        Box {
            if (!trailerId.isNullOrEmpty()) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                600
                            )
                            settings.javaScriptEnabled = true
                            loadUrl("https://www.youtube.com/embed/$trailerId")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 16.dp)
                )
            }
            else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Trailer not available", color = Color.White)
                }
            }
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.Cast, contentDescription = "Cast", tint = Color.White)
                    }
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Download", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .zIndex(1f)
            )

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.DarkGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(4.dp)
                    .background(Color.Red)
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(movie.title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(movie.year, color = Color.Gray, fontSize = 14.sp)
                Text("  ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    movie.rating,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .background(Color.DarkGray, shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
                Text("  ", color = Color.Gray, fontSize = 14.sp)
                Text(movie.duration, color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(Modifier.height(8.dp))
            Text("Watch in ${movie.languages.joinToString()}", color = Color.White, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RectangleShape
            ) {
                Icon(Icons.Default.PlayArrow, modifier = Modifier.size(30.dp), contentDescription = null, tint = Color.Black)
                Spacer(Modifier.width(6.dp))
                Text("Play", color = Color.Black, fontSize = 20.sp)
            }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { },
                colors = ButtonDefaults.buttonColors(Color(0xFF1C1C1E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RectangleShape
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Download", color = Color.White, fontSize = 20.sp)
            }
            Spacer(Modifier.height(16.dp))
            Text(movie.description, color = Color.White)
            Spacer(Modifier.height(8.dp))
            FlowRow {
                Text(
                    text = "Starring: ${movie.cast.take(2).joinToString()}",
                    color = Color.Gray
                )
                if (movie.cast.size > 2) {
                    Text(
                        text = "...more",
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                navController.navigate("castdetailscreen/${movie.Imdbid}")
                            }
                    )
                }
            }
            Text("Director: ${movie.director}", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Add, contentDescription = "My List", tint = Color.White, modifier = Modifier.size(35.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("My List", color = Color.White, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.ThumbUp,
                        contentDescription = "Rate",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                viewModel.likeMovie(apiService, movie)
                            }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Rate", color = Color.White, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Share", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}
