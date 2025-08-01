package com.example.cineflix.Screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.material3.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import coil.compose.rememberAsyncImagePainter
import com.example.cineflix.R

@Composable
fun SplashScreen(navController: NavController) {
    val letterImages = listOf(
        R.drawable.nlogo,
        R.drawable.elogo,
        R.drawable.tlogo,
        R.drawable.flogo,
        R.drawable.llogo,
        R.drawable.ilogo,
        R.drawable.xlogo
    )
    var visibleLettersCount by remember { mutableStateOf(0) }

    // Reveal one image at a time
    LaunchedEffect(Unit) {
        for (i in letterImages.indices) {
            delay(320)
            visibleLettersCount = i + 1
        }
        delay(1500)
        navController.navigate("NetflixSimpleWelcomeScreen") {
            popUpTo("Splashscreen") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Row(
            //horizontalArrangement = Arrangement.spacedBy(0.dp), // No added space
            verticalAlignment = Alignment.CenterVertically
        ) {
            letterImages.forEachIndexed { index, imageRes ->
                AnimatedVisibility(visible = index < visibleLettersCount) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageRes),
                        contentDescription = "Letter $index",
                        modifier = Modifier
                            .size(50.dp) // Slightly smaller

                    )
                }
            }
        }

    }
}

