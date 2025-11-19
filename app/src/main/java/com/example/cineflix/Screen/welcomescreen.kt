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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun NetflixSimpleWelcomeScreen(navController: NavHostController) {
    val imageList = listOf(
        R.drawable.gs1,
        R.drawable.gs2,
        R.drawable.gs3
    )

    var currentImageIndex by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navController.navigate("HomeScreen") {
                popUpTo("WelcomeScreen") { inclusive = true }
            }
        }
    }


    LaunchedEffect(Unit) {
        while (true) {
            delay(4000L)
            currentImageIndex = (currentImageIndex + 1) % imageList.size
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                .padding(horizontal = 36.dp, vertical = 36.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PRIVACY",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { openUrl("https://help.netflix.com/en/node/100628") }
            )
            Text(
                text = "SIGN IN",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { navController.navigate("loginscreen") }
            )
        }


        // Text & dots over image (Bottom Center)
        // Overlay layout
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Centered text block (middle of the screen)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter).padding(bottom = 95.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.SpaceBetween
            ) {
                //Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text = "Unlimited ",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "entertainment,",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "one low price.",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "All of Netflix, starting at just \n ₹149.",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Bottom section: Dots + Button
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    imageList.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (index == currentImageIndex) Color.White else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("Signupscreen") },
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
}
