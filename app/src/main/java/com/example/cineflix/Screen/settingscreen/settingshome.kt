package com.example.cineflix.Screen.settingscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineflix.R
import com.example.cineflix.Screen.Homescreen.BottomBar
import com.example.cineflix.Screen.settingscreen.BottomSheetItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNetflixScreen() {
    val likedMovies = listOf("Leo", "Yeh Jawaani Hai Deewani", "Devara")
    val moviePosters = listOf(
        R.drawable.prof,
        R.drawable.prof,
        R.drawable.prof
    )
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Netflix",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Cast, contentDescription = "Cast", tint = Color.White)
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
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
        bottomBar = {
            BottomBar(selected = "My Netflix") // Ensure "My Netflix" is selected
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
                .verticalScroll(rememberScrollState())

        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Profile section (centered avatar with name below)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.prof),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "shrikant",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Notifications card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)), // dark gray-ish
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Notifications",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "See All",
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Arrow",
                            tint = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "TV Shows & Movies You have Liked",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                //contentPadding = PaddingValues(horizontal = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp)

            ) {
                itemsIndexed(likedMovies) { index, title ->
                    Card(
                        modifier = Modifier
                            .width(120.dp)
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = moviePosters[index]),
                                contentDescription = title,
                                modifier = Modifier
                                    .height(160.dp).padding(bottom = 10.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                                        contentScale = ContentScale.Crop)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "Share",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Share",
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
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
                    NetflixBottomSheetContent()
                }
            }

        }
    }
}

    @Composable
    fun NetflixBottomSheetContent() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        ) {

            //Spacer(modifier = Modifier.height(8.dp))
            BottomSheetItem(icon = Icons.Outlined.Edit, text = "Manage Profiles")
            BottomSheetItem(icon = Icons.Outlined.Settings, text = "App Settings")
            BottomSheetItem(icon = Icons.Outlined.AccountBox, text = "Account")
            BottomSheetItem(icon = Icons.Default.Help, text = "Help")
            BottomSheetItem(icon = Icons.Default.ExitToApp, text = "Sign Out", onClick = { })

            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Version: 9.22.1 build 3",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(15.dp))
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



@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun MyNetflixScreenPreview() {
    MyNetflixScreen()
}


