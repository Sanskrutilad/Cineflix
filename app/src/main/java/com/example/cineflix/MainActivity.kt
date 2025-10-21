package com.example.cineflix

import UploadProfileScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cineflix.Navigation.Navigation
import com.example.cineflix.Retrofit.createApiService
import com.example.cineflix.ui.theme.CineflixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = createApiService()
        enableEdgeToEdge()
        setContent {
            CineflixTheme {
                UploadProfileScreen(
                    userId = "U124",
                    profileId = "P124",
                    profileName = "Shrikant",
                    isChildrenProfile = false,
                    apiService
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
