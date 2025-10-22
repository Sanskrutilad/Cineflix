package com.example.cineflix.Screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.cineflix.R
import com.example.cineflix.Viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhosWatchingScreen(navController: NavHostController, profileViewModel: ProfileViewModel) {
    val profiles = profileViewModel.profiles

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Profiles",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(profiles) { profile ->
                    ProfileItem(profile = profile, onClick = { navController.navigate("HomeScreen") })
                }
                item {
                    AddProfileItem(onClick = { navController.navigate("AddProfileScreen") })
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileItem(profile: Profile, onClick: () -> Unit) {
    var showEdit by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
                .combinedClickable(
                    onClick = {
                        if (!showEdit) {
                            onClick()
                        } else {

                        }
                    },
                    onLongClick = { showEdit = !showEdit }
                )
        ) {
            Image(
                painter = when {
                    profile.imageUrl != null -> rememberAsyncImagePainter(profile.imageUrl)
                    profile.imageRes != null -> painterResource(id = profile.imageRes)
                    else -> painterResource(id = R.drawable.profileicon)
                },
                contentDescription = profile.name,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            if (showEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = profile.name, color = Color.White)
    }
}

@Composable
fun AddProfileItem(onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Profile",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Add Profile", color = Color.White)
    }
}
data class Profile(
    val name: String,
    val imageUrl: String? = null,
    val imageRes: Int? = null,
    val profileId: String = ""
)





