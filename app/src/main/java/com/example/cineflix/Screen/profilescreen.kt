package com.example.cineflix.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineflix.R

@Composable
fun ProfileScreen() {
    val profiles = listOf("Sanskruti" to R.drawable.prof, "Shrikant" to R.drawable.prof)
    val menuItems = listOf("Notifications", "My List", "App Settings", "Account", "Help")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text("Profiles & More", color = Color.White, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow {
            items(profiles) { (name, imageRes) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(name, color = Color.White, fontSize = 15.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Manage Profiles", color = Color.LightGray, fontSize = 16.sp , textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        menuItems.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth().height(70.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                    .clickable { }
                    .padding(16.dp)
            ) {
                Text(item, color = Color.White , fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Sign Out",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
