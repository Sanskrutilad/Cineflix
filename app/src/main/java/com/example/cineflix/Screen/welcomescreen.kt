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
import androidx.compose.ui.text.font.FontWeight
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
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "N",
                color = Color.Red,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                Text(
                    text ="PRIVACY",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text ="SIGN IN",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    Log.d("NetflixScreen", "Get Started button clicked")
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
