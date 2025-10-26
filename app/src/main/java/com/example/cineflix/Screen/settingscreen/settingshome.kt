package com.example.cineflix.Screen.settingscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cineflix.R
import com.example.cineflix.Retrofit.ApiService
import com.example.cineflix.Screen.Homescreen.BottomBar
import com.example.cineflix.Screen.fetchUserLogo
import com.example.cineflix.Screen.uploadLogo
import com.example.cineflix.Viewmodel.LikedMovie
import com.example.cineflix.Viewmodel.LikedMoviesViewModel
import com.example.cineflix.Viewmodel.MyListMovie
import com.example.cineflix.Viewmodel.MyListViewModel
import com.example.cineflix.Viewmodel.WatchedTrailersViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settingmainscreen(
    apiService: ApiService,
    navController: NavHostController,
    likedMoviesViewModel: LikedMoviesViewModel = viewModel(),
    watchedViewModel: WatchedTrailersViewModel = viewModel(),
    myListViewModel: MyListViewModel = viewModel()
) {
    var myListMovies by remember { mutableStateOf<List<LikedMovie>>(emptyList()) }
    var isLoadingLiked by remember { mutableStateOf(true) }
    var myList by remember { mutableStateOf<List<MyListMovie>>(emptyList()) }
    var isLoadingMyList by remember { mutableStateOf(true) }
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid ?: ""
    var uploadedLogoUrl by remember { mutableStateOf<String?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fetchUserLogo(apiService, userId) { logoUrl ->
            uploadedLogoUrl = logoUrl
        }
        myListViewModel.getMyList { list ->
            myList = list
            isLoadingMyList = false
        }
        likedMoviesViewModel.getLikedMovies { list ->
            myListMovies = list
            isLoadingLiked = false
        }
    }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isUploading = true
            uploadLogo(
                context = context,
                apiService = apiService,
                logoUri = it,
                userId = userId
            ) { success, url ->
                isUploading = false
                if (success) {
                    Toast.makeText(context, "Profile photo updated!", Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.IO).launch {
                        fetchUserLogo(apiService, userId) { logoUrl ->
                            uploadedLogoUrl = logoUrl
                        }
                    }
                } else {
                    Toast.makeText(context, "Upload failed, try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    val watchedTrailers by watchedViewModel.watchedList.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Netflix", fontWeight = FontWeight.Bold, color = Color.White) },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Cast, contentDescription = "Cast", tint = Color.White)
                    }
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            showBottomSheet = true
                            sheetState.show()
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Black),
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            if (!isUploading) imagePickerLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isUploading -> {
                            CircularProgressIndicator(color = Color.Red, strokeWidth = 3.dp)
                        }
                        !uploadedLogoUrl.isNullOrEmpty() -> {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uploadedLogoUrl)
                                    .memoryCachePolicy(CachePolicy.DISABLED)
                                    .diskCachePolicy(CachePolicy.DISABLED)
                                    .networkCachePolicy(CachePolicy.ENABLED)
                                    .build(),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop,
                                onError = { result ->
                                    Log.e("AsyncImage", "Image load failed: ${result.result.throwable.message}")
                                    Log.e("AsyncImage", "URL tried: $uploadedLogoUrl")
                                },
                                error = painterResource(R.drawable.prof)
                            )

                        }
                        else -> {
                            Log.d("AsyncImage", "Showing fallback image")
                            Image(
                                painter = painterResource(id = R.drawable.prof),
                                contentDescription = "Profile",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Sanskruti", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            SectionWithMovies(
                title = "TV Shows & Movies You Liked",
                isLoading = isLoadingLiked,
                emptyMessage = "No liked movies yet",
                movies = myListMovies.map { MovieUi(it.title ?: "", it.poster ?: "", it.imdbId) },
                context = context,
                showShare = true,
                navController = navController
            )
            Spacer(modifier = Modifier.height(20.dp))
            SectionWithMovies(
                title = "My List",
                isLoading = isLoadingMyList,
                emptyMessage = "Your My List is empty",
                movies = myList.map { MovieUi(it.title ?: "", it.poster ?: "", it.imdbId) },
                context = context,
                navController = navController
            )
            Spacer(modifier = Modifier.height(20.dp))
            SectionWithMovies(
                title = "Trailers You Watched",
                isLoading = false,
                emptyMessage = "No trailers watched yet",
                movies = watchedTrailers.map { MovieUi(it.title, it.poster, it.imdbId) },
                context = context,
                navController = navController
            )
            Spacer(modifier = Modifier.height(100.dp))
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        coroutineScope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                        }
                    },
                    sheetState = sheetState,
                    containerColor = Color(0xFF1C1C1E),
                    tonalElevation = 4.dp
                ) {
                    NetflixBottomSheetContent(navController)
                }
            }
        }
    }
}

data class MovieUi(
    val title: String,
    val poster: String,
    val imdbId: String? = null
)

@Composable
fun SectionWithMovies(
    title: String,
    isLoading: Boolean,
    emptyMessage: String,
    movies: List<MovieUi>,
    context: Context,
    showShare: Boolean = false,
    navController: NavHostController
) {
    Text(title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        when {
            isLoading -> item {
                Text("Loading...", color = Color.Gray, modifier = Modifier.padding(16.dp))
            }
            movies.isEmpty() -> item {
                Text(emptyMessage, color = Color.Gray, modifier = Modifier.padding(16.dp))
            }
            else -> items(movies) { movie ->
                Card(
                    modifier = Modifier.width(120.dp).wrapContentHeight().clickable{navController.navigate("MoviedetailScreen/${movie.imdbId}")},
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = movie.poster,
                            contentDescription = movie.title,
                            modifier = Modifier
                                .height(160.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (showShare) {
                            // Show share row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth().clickable {
                                    movie.imdbId?.let { imdbId ->
                                        val movieLink = "https://www.imdb.com/title/$imdbId"
                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                "Check out this movie: ${movie.title}\n\n$movieLink"
                                            )
                                        }
                                        context.startActivity(
                                            Intent.createChooser(shareIntent, "Share via")
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Outlined.Share,
                                    contentDescription = "Share",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share", color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NetflixBottomSheetContent(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
    ) {
        BottomSheetItem(
            icon = Icons.Outlined.Edit,
            text = "Manage Profiles",
            onClick = { navController.navigate("WhosWatchingScreen") })
        BottomSheetItem(
            icon = Icons.Outlined.Settings,
            text = "App Settings",
            onClick = { navController.navigate("appsettings") })
        BottomSheetItem(
            icon = Icons.Default.Help,
            text = "Help",
            onClick = { navController.navigate("Helpscreen") })
        BottomSheetItem(
            icon = Icons.Default.ExitToApp,
            text = "Sign Out",
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("NetflixSimpleWelcomeScreen") {
                    popUpTo(0) { inclusive = true }
                }
            })
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
fun BottomSheetItem(icon: ImageVector, text: String, onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontSize = 19.sp
        )
    }
}
