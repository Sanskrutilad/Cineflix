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

@Composable
fun UploadProfileScreen(
    userId: String,
    profileId: String,
    profileName: String,
    isChildrenProfile: Boolean,
    apiService: ApiService
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedUrl by remember { mutableStateOf<String?>(null) }
    var fetchedPhotoUrl by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf("") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        Log.d("UploadProfile", "📸 Image selected: $uri")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Upload & View Profile", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(20.dp))

        selectedImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { imagePicker.launch("image/*") },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Select Image")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    selectedImageUri?.let { uri ->
                        try {
                            Log.d("UploadProfile", "🚀 Upload started for userId: $userId")

                            val file = File(context.cacheDir, "temp.jpg")
                            context.contentResolver.openInputStream(uri)?.use { input ->
                                file.outputStream().use { output -> input.copyTo(output) }
                            }

                            val imagePart = MultipartBody.Part.createFormData(
                                "image", file.name,
                                RequestBody.create("image/*".toMediaTypeOrNull(), file)
                            )

                            val response = apiService.uploadProfile(
                                imagePart,
                                RequestBody.create("text/plain".toMediaTypeOrNull(), userId),
                                RequestBody.create("text/plain".toMediaTypeOrNull(), isChildrenProfile.toString()),
                                RequestBody.create("text/plain".toMediaTypeOrNull(), profileName),
                                RequestBody.create("text/plain".toMediaTypeOrNull(), profileId)
                            )

                            Log.d("UploadProfile", "📡 Upload response: ${response.raw()}")

                            if (response.isSuccessful) {
                                uploadedUrl = response.body()?.profileUrl
                                message = response.body()?.message ?: "Uploaded"
                                Log.d("UploadProfile", "✅ Uploaded Successfully: $uploadedUrl")
                            } else {
                                message = "Upload failed"
                                Log.e("UploadProfile", "❌ Upload failed: ${response.errorBody()?.string()}")
                            }
                        } catch (e: Exception) {
                            Log.e("UploadProfile", "💥 Upload Exception: ${e.message}", e)
                        }
                    } ?: run {
                        message = "No image selected"
                        Log.w("UploadProfile", "⚠️ No image selected for upload")
                    }
                }
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Upload Profile")
        }

        Spacer(modifier = Modifier.height(15.dp))

        uploadedUrl?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
        }

        Text(message)
        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        Log.d("UploadProfile", "🔍 Fetching profile for userId: $userId")

                        val res = apiService.getProfilePhoto(userId)
                        Log.d("UploadProfile", "📡 Fetch response: ${res.raw()}")

                        if (res.isSuccessful) {
                            fetchedPhotoUrl = res.body()?.user?.photoUrl
                            message = "Fetched profile successfully"
                            Log.d("UploadProfile", "✅ Fetched Photo URL: $fetchedPhotoUrl")
                        } else {
                            message = "Failed to fetch profile"
                            Log.e("UploadProfile", "❌ Fetch failed: ${res.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        Log.e("UploadProfile", "💥 Fetch Exception: ${e.message}", e)
                        message = "Fetch error"
                    }
                }
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Get Profile Photo")
        }

        Spacer(modifier = Modifier.height(15.dp))

        fetchedPhotoUrl?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
        }
    }
}
