package com.example.cineflix.Screen.Homescreen

import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.cineflix.Retrofit.ApiService
import com.example.cineflix.Viewmodel.LikedMovie
import com.example.cineflix.Viewmodel.LikedMoviesViewModel
import com.example.cineflix.Viewmodel.MyListMovie
import com.example.cineflix.Viewmodel.MyListViewModel
import com.example.cineflix.Viewmodel.NetflixViewModel
import com.example.cineflix.Viewmodel.WatchedTrailer
import com.example.cineflix.Viewmodel.WatchedTrailersViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NetflixTopBarScreen(navController: NavHostController) {
    val context = LocalContext.current
    val movieViewModel: NetflixViewModel = viewModel()
    val bollywood = movieViewModel.bollywood

    if (bollywood.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Gray)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Loading..\nThis may take a while.",
                    color = Color.White,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }
    var order by remember(bollywood) { mutableStateOf(bollywood.shuffled()) }
    var index by remember { mutableIntStateOf(0) }
    val featuredMovie = order[index]

    var backgroundColor by remember { mutableStateOf(Color.DarkGray) }

    // Extract color when poster changes
    LaunchedEffect(featuredMovie.Poster) {
        extractDominantColorFromUrl(context, featuredMovie.Poster) { c ->
            backgroundColor = c
        }
    }
    // Auto-rotate
    LaunchedEffect(order) {
        while (true) {
            delay(7000) // 7s
            index = (index + 1) % order.size
            if (index == 0) order = bollywood.shuffled()
        }
    }
    val scrollState = rememberLazyListState()
    val showChips by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex == 0 &&
                    scrollState.firstVisibleItemScrollOffset < 20
        }
    }
    Scaffold(
        containerColor = Color.Black,
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier.padding(paddingValues)
        ) {
            stickyHeader {
                TopAppBarContent(backgroundColor,navController)
            }
            item {
                AnimatedVisibility(
                    visible = showChips,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    FilterChipsRow(backgroundColor, navController)
                }
            }
            item {
                FeaturedBanner(
                    navController = navController,
                    backgroundColor = backgroundColor,
                    featuredMovie = featuredMovie,
                )
            }
            item { MobileGamesSection(backgroundColor, navController ) }
            item { NetflixHomeScreen(navController) }
        }
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class
)
@Composable
fun MovieDetailScreen(
    navController: NavHostController,
    Imdb: String,
    apiService: ApiService,
    viewModel: NetflixViewModel = viewModel(),
    myListViewModel: MyListViewModel = viewModel(),
     watchedViewModel: WatchedTrailersViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val movie = viewModel.selectedMovie
    val trailerId = viewModel.trailerId
    val isInList = remember { mutableStateOf(false) }

    // Fetch movie by ID
    LaunchedEffect(Imdb) {
        viewModel.fetchMovieById(Imdb)
    }
    // Fetch trailer after movie is loaded
    LaunchedEffect(movie?.Imdbid) {
        movie?.Imdbid?.let { imdbId ->
            // Fetch trailer
            viewModel.fetchTrailerByImdbId(imdbId)
            myListViewModel.isMovieInMyList(imdbId) { inList ->
                isInList.value = inList
            }
        }
    }

    val likedViewModel: LikedMoviesViewModel = viewModel()
    val isLiked = remember { mutableStateOf(false) }

    LaunchedEffect(movie?.Imdbid) {
        movie?.Imdbid?.let { id ->
            likedViewModel.isMovieLiked(id) { exists ->
                isLiked.value = exists
            }
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
            if (trailerId != null) {
                if (trailerId.isNotEmpty()) {
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
                } else {
                    // Only show this if trailerId is known to be empty
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Trailer not available", color = Color.White)
                    }
                }
            }
            else {
                // While trailerId is null (still loading), show a loading spinner instead of flickering
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Red)
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
                onClick = {
                    movie.Imdbid?.let { imdb ->
                        val trailer = WatchedTrailer(
                            imdbId = imdb,
                            title = movie.title,
                            poster = movie.Poster ?: ""
                        )
                        watchedViewModel.addWatchedTrailer(trailer)
                        Log.d("MovieDetailScreen", "Trailer added to history ✅: ${movie.title}")
                    }
                },
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
                    Icon(
                        imageVector = if (isInList.value) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = "My List",
                        tint = Color.White,
                        modifier = Modifier
                            .size(35.dp)
                            .clickable {
                                val imdb = movie.Imdbid ?: return@clickable
                                if (isInList.value) {
                                    // Remove from My List
                                    myListViewModel.removeMovieFromMyList(imdb) { success ->
                                        if (success) {
                                            isInList.value = false
                                            Log.d("MovieDetailScreen", "Removed from My List ❌: ${movie.title}")
                                        }
                                    }
                                } else {
                                    // Add to My List
                                    val movieObj = MyListMovie(
                                        imdbId = imdb,
                                        title = movie.title,
                                        poster = movie.Poster ?: ""
                                    )
                                    myListViewModel.addMovieToMyList(movieObj) { success ->
                                        if (success) {
                                            isInList.value = true
                                            Log.d("MovieDetailScreen", "Added to My List ✅: ${movie.title}")
                                        }
                                    }
                                }
                            }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isInList.value) "Added" else "My List",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        if (isLiked.value) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "Like",
                        tint = if (isLiked.value) Color.White else Color.White,
                        modifier = Modifier.size(30.dp).clickable {
                            movie.Imdbid?.let { id ->
                                val movieObj = LikedMovie(id, movie.title ?: "", movie.Poster ?: "")
                                if (isLiked.value) {
                                    likedViewModel.removeMovieFromLiked(id) { success ->
                                        if (success) {
                                            isLiked.value = false
                                            Log.d("MovieDetailScreen", "Movie unliked ❌: ${movie.title}")
                                        } else {
                                            Log.e("MovieDetailScreen", "Failed to unlike movie: ${movie.title}")
                                        }
                                    }
                                } else {
                                    likedViewModel.addMovieToLiked(movieObj) { success ->
                                        if (success) {
                                            isLiked.value = true
                                            Log.d("MovieDetailScreen", "Movie liked ✅: ${movie.title}")
                                        } else {
                                            Log.e("MovieDetailScreen", "Failed to like movie: ${movie.title}")
                                        }
                                    }
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Like", color = Color.White, fontSize = 12.sp)
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

