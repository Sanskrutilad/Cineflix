package com.example.cineflix.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cineflix.R

@Composable
fun ProfileScreen() {
    val profiles = listOf("Sanskruti" to R.drawable.prof, "Shrikant" to R.drawable.prof)

    val menuItems = listOf(
        "Notifications" to Icons.Default.Notifications,
        "My List" to Icons.Default.List,
        "App Settings" to Icons.Default.Settings,
        "Account" to Icons.Default.AccountCircle,
        "Help" to  Icons.Default.Info
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Profiles & More",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.weight(6f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f)) // for symmetry with the icon
        }
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
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Manage Profiles",
            color = Color.LightGray,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        menuItems.forEach { (label, icon) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color(0xFF433C3C), shape = RoundedCornerShape(8.dp))
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 15.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "$label icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(label, color = Color.White, fontSize = 20.sp)
                    }
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = "Navigate",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        Spacer(modifier = Modifier.height(100.dp))

        Text(
            "Sign Out",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable{ }
                .padding(16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}