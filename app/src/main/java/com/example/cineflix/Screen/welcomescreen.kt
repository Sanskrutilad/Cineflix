package com.example.cineflix.Screen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.cineflix.R
import kotlinx.coroutines.delay

@Composable
fun NetflixSimpleWelcomeScreen(navController: NavHostController) {
    val imageList = listOf(
        R.drawable.img05,
        R.drawable.img06,
        R.drawable.img04
    )
    val context = LocalContext.current
    var currentImageIndex by remember { mutableStateOf(0) }
    // Auto change every 4s
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000L)
            currentImageIndex = (currentImageIndex + 1) % imageList.size
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = imageList[currentImageIndex]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        fun openUrl(url: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("NetflixWelcome", "Failed to open URL: $url", e)
            }
        }
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(
                    text = "PRIVACY",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 16.dp).clickable{openUrl("https://help.netflix.com/en/node/100628")}
                )
                Text(
                    text = "SIGN IN",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier=Modifier.clickable{navController.navigate("loginscreen")}
                )
            }
        }

        // Text & dots over image (Bottom Center)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dot Indicators
            Row(horizontalArrangement = Arrangement.Center) {
                imageList.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (index == currentImageIndex) Color.White else Color.Gray)
                            .padding(4.dp)
                            .then(Modifier.padding(horizontal = 4.dp))
                    )
                    Spacer(Modifier.width(30.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Get Started Button
            Button(
                onClick = { navController.navigate("ReadyToWatchScreen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            ) {
                Text(
                    text = "Get Started",
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}
