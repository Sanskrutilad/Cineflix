package com.example.cineflix.Screen.settingscreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.cineflix.R
import com.example.cineflix.Screen.Homescreen.BottomBar
import com.example.cineflix.Viewmodel.LikedMovie
import com.example.cineflix.Viewmodel.LikedMoviesViewModel
import com.example.cineflix.Viewmodel.MyListMovie
import com.example.cineflix.Viewmodel.MyListViewModel
import com.example.cineflix.Viewmodel.WatchedTrailersViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settingmainscreen(
    navController: NavHostController,
    likedMoviesViewModel: LikedMoviesViewModel = viewModel(),
    watchedViewModel: WatchedTrailersViewModel = viewModel()
) {
    var myListMovies by remember { mutableStateOf<List<LikedMovie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        likedMoviesViewModel.getLikedMovies { list ->
            myListMovies = list
            isLoading = false
        }
    }

    // Collect watched trailers from ViewModel
    val watchedTrailers by watchedViewModel.watchedList.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Netflix", fontWeight = FontWeight.Bold, color = Color.White) },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Cast, contentDescription = "Cast", tint = Color.White) }
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White) }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            showBottomSheet = true
                            sheetState.show()
                        }
                    }) { Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White) }
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

            // Profile
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.prof),
                    contentDescription = "Profile",
                    modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Shrikant", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Notifications card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 1.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Notifications", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("See All", color = Color.Gray, fontWeight = FontWeight.Medium)
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Arrow", tint = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Liked Movies Section
            Text("TV Shows & Movies You have Liked", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                when {
                    isLoading -> item { Text("Loading...", color = Color.Gray, modifier = Modifier.padding(16.dp)) }
                    myListMovies.isEmpty() -> item { Text("No liked movies yet", color = Color.Gray, modifier = Modifier.padding(16.dp)) }
                    else -> items(myListMovies) { likedMovie ->
                        Card(
                            modifier = Modifier.width(120.dp).wrapContentHeight(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxWidth()) {
                                AsyncImage(
                                    model = likedMovie.poster,
                                    contentDescription = likedMovie.title,
                                    modifier = Modifier.height(160.dp).fillMaxWidth().clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().clickable{}) {
                                    Icon(Icons.Outlined.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Share", color = Color.White, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Watched Trailers Section
            Text("Trailers You Watched", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                if (watchedTrailers.isEmpty()) {
                    item { Text("No trailers watched yet", color = Color.Gray, modifier = Modifier.padding(16.dp)) }
                } else {
                    items(watchedTrailers) { trailer ->
                        Card(
                            modifier = Modifier.width(200.dp).wrapContentHeight(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxWidth()) {
                                AsyncImage(
                                    model = trailer.poster,
                                    contentDescription = trailer.title,
                                    modifier = Modifier.height(120.dp).fillMaxWidth().clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = trailer.title,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))

            // Bottom sheet
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



val recentlyWatched = listOf(
    Triple(R.drawable.prof, "Stranger Things", "S2:E5 Dig Dug"),
    Triple(R.drawable.prof, "Alice in Borderland", "S1:E1 Episode One"),
    Triple(R.drawable.prof, "Money Heist", "S3:E2 48 Hours Left")
)

@Composable
fun RecentlyWatchedCard(
    @DrawableRes imageRes: Int,
    title: String,
    episode: String
) {
    Column(
        modifier = Modifier
            .padding(end = 8.dp)
    ) {
        Box(modifier = Modifier.height(180.dp).width(270.dp)) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier
                        .size(34.dp)
                        .clickable { }
                )
                Spacer(modifier = Modifier.width(18.dp))
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = Color.White,
                    modifier = Modifier
                        .size(34.dp)
                        .clickable {  }
                )
            }
        }
        Text(
            text = episode,
            color = Color.Gray,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun NetflixBottomSheetContent(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
        ) {
            BottomSheetItem(icon = Icons.Outlined.Edit, text = "Manage Profiles",onClick = { navController.navigate("whoswatching")})
            BottomSheetItem(icon = Icons.Outlined.Settings, text = "App Settings",onClick = { navController.navigate("appsettings") })
            BottomSheetItem(icon = Icons.Default.Help, text = "Help",onClick = { navController.navigate("Helpscreen") })
            BottomSheetItem(icon = Icons.Default.ExitToApp, text = "Sign Out", onClick = { FirebaseAuth.getInstance().signOut()
                navController.navigate("NetflixSimpleWelcomeScreen") {
                    popUpTo(0) { inclusive = true }
                }})
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
            Icon(imageVector = icon, contentDescription = text, tint = Color.White, modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontSize = 19.sp
            )
        }
    }





