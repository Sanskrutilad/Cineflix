package com.example.cineflix.Navigation

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import com.example.cineflix.Retrofit.ApiService

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import com.example.cineflix.Retrofit.ProfileItem
import com.example.cineflix.Screen.Profile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(apiService: ApiService) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    var profile by remember { mutableStateOf<ProfileItem?>(null) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        if (userId != null) {
            loading = true
            error = null
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getProfilePhoto(userId)
                }
                if (response.isSuccessful && response.body()?.success == true) {
                    profile = response.body()?.profile
                } else {
                    error = "Profile not found."
                }
            } catch (e: Exception) {
                error = e.localizedMessage ?: "An error occurred."
            } finally {
                loading = false
            }
        } else {
            error = "User not logged in."
        }
    }

    // 🧩 UI Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            loading -> CircularProgressIndicator()

            error != null -> Text(
                text = "❌ $error",
                color = Color.Red,
                textAlign = TextAlign.Center
            )

            profile != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = profile!!.photoUrl,
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = profile!!.profileName,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Profile ID: ${profile!!.profileId}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = if (profile!!.isChildrenProfile) "Children Profile" else "Adult Profile",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Uploaded At: ${profile!!.uploadedAt}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            else -> Text("No profile data available.")
        }
    }
}
