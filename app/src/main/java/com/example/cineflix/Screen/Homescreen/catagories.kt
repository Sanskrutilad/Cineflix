@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.cineflix.Screen.Homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun PreviewCategoryScreen(navController: NavHostController) {
    val sampleCategories = listOf(
        "Home", "My List", "Available for Download", "The Summer Edition",
        "Celebrating Disability with Dimension", "Hindi", "Tamil", "Punjabi",
        "Telugu", "Malayalam", "Marathi", "Bengali", "English", "Action",
        "Anime", "Award Winners", "Biographical", "Blockbusters", "Bollywood",
        "Kids & Family", "Comedies", "Documentaries", "Dramas", "Fantasy",
        "Hollywood", "Horror", "International", "Indian", "Music & Musicals",
        "Reality & Talk", "Romance", "Sci-Fi", "Stand-Up", "Thrillers",
        "United States", "Audio Description in English"
    )
    CategoryScreen(categories = sampleCategories, onClose = {},navController)
}

@Composable
fun CategoryScreen(categories: List<String>, onClose: () -> Unit, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            items(categories) { category ->
                Text(
                    text = category,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            IconButton(
                onClick = {navController.popBackStack()},
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }
        }
    }
}


