package com.example.cineflix.Screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cineflix.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController : NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.netflixlogo),
                        contentDescription = "Netflix Logo",
                        modifier = Modifier
                            .height(90.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate("Signupscreen") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Help Section
            Text(
                text = "Find Help Online",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Divider(color = Color.LightGray, thickness = 1.dp)
            HelpOption("Help Centre", Icons.Default.Info, "https://help.netflix.com/")
            Divider(color = Color.LightGray, thickness = 1.dp)
            HelpOption("Request a title", Icons.Default.Star, "https://help.netflix.com/en/titlerequest")
            Divider(color = Color.LightGray, thickness = 1.dp)
            HelpOption("Fix a connection problem", Icons.Default.Settings, "https://help.netflix.com/en/node/306")
            Divider(color = Color.LightGray, thickness = 1.dp)
            HelpOption("Privacy Statement", Icons.Default.Lock, "https://help.netflix.com/legal/privacy")
            Divider(color = Color.LightGray, thickness = 1.dp)
            HelpOption("Terms of Use", Icons.Default.Star, "https://help.netflix.com/legal/termsofuse")
            Divider(color = Color.LightGray, thickness = 1.dp)

            Spacer(modifier = Modifier.height(150.dp))

            // Contact section
            Text(
                text = "Contact\nNetflix Customer Services",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "We’ll connect the call for free using your\ninternet connection.",
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Call and Chat Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0000000000")) // replace with real number
                    context.startActivity(intent)
                },colors = ButtonDefaults.buttonColors(Color.Black)) {
                    Icon(Icons.Default.Call, contentDescription = "Call")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CALL")
                }

                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/en/contactus"))
                    context.startActivity(intent)
                },colors = ButtonDefaults.buttonColors(Color.Black)) {
                    Icon(Icons.Default.Email, contentDescription = "Chat")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CHAT")
                }
            }
        }
    }
}

@Composable
fun HelpOption(text: String, icon: ImageVector, url: String) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF1976D2)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp , color = Color(0xFF1976D2) )
    }
}
