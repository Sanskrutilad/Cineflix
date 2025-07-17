package com.example.cineflix.Screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.cineflix.R

@Composable
fun NetflixSimpleWelcomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Try to safely load the background image
        val imageRes = try {
            R.drawable.screenshot_2025_07_17_at_7_30_56pm
        } catch (e: Exception) {
            Log.e("NetflixScreen", "Error loading image", e)
            0
        }
        if (imageRes != 0) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        } else {
            Log.e("NetflixScreen", "Image resource not found: R.drawable.netlogo")
        }

        // Red "Get Started" Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    Log.d("NetflixScreen", "Get Started button clicked")
                    // TODO: Add navigation or action
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Get Started", color = Color.White, style = TextStyle(fontSize = 16.sp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNetflixSimpleWelcomeScreen() {
    NetflixSimpleWelcomeScreen()
}
