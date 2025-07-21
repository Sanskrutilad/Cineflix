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
import com.example.cineflix.R

@Composable
fun SplashScreen(navController: NavController) {
    var visible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.95f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    // Trigger animation and navigate after delay
    LaunchedEffect(Unit) {
        visible = true
        delay(3000) // Total splash time
        navController.navigate("NetflixSimpleWelcomeScreen") {
            popUpTo("Splashscreen") { inclusive = true }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Netflix uses solid black background
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.netflixlogo),
            contentDescription = "Netflix Logo",
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
                .size(160.dp) // Netflix's splash logo is medium size
        )
    }
}
