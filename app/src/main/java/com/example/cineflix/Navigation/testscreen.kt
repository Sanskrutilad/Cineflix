//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.platform.LocalContext
//import coil.compose.rememberAsyncImagePainter
//import com.example.cineflix.Retrofit.ApiService
//import kotlinx.coroutines.launch
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import java.io.File
//
//@Composable
//fun ProfilePhotoScreenDynamic(
//    apiService: ApiService
//) {
//    val scope = rememberCoroutineScope()
//    val context = LocalContext.current
//
//    var userId by remember { mutableStateOf("") }
//    var photoUrl by remember { mutableStateOf<String?>(null) }
//    var resultText by remember { mutableStateOf("") }
//
//    // Image picker
//    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        selectedImageUri = uri
//    }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        OutlinedTextField(
//            value = userId,
//            onValueChange = { userId = it },
//            label = { Text("Enter User ID") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = { launcher.launch("image/*") }) {
//            Text("Pick Image")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = {
//            if (userId.isEmpty()) {
//                resultText = "Please enter User ID"
//                return@Button
//            }
//
//            selectedImageUri?.let { uri ->
//                val file = File(uri.path!!) // Proper URI → File conversion needed in real app
//                scope.launch {
//                    try {
//                        val userIdBody = RequestBody.create(
//                            "text/plain".toMediaTypeOrNull(),
//                            userId
//                        )
//                        val body = MultipartBody.Part.createFormData(
//                            "photo",
//                            file.name,
//                            file.asRequestBody("image/*".toMediaTypeOrNull())
//                        )
//
//                        val response = apiService.uploadProfilePhoto(body, userIdBody)
//                        if (response.isSuccessful && response.body()?.success == true) {
//                            photoUrl = response.body()?.photoUrl
//                            resultText = "Photo uploaded successfully!"
//                        } else {
//                            resultText = "Upload failed: ${response.body()?.let { it } ?: response.message()}"
//                        }
//                    } catch (e: Exception) {
//                        resultText = "Error: ${e.message}"
//                    }
//                }
//            }
//        }) {
//            Text("Upload Photo")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = {
//            if (userId.isEmpty()) {
//                resultText = "Please enter User ID"
//                return@Button
//            }
//
//            scope.launch {
//                try {
//                    val response = apiService.getProfilePhoto(userId)
//                    if (response.isSuccessful && response.body()?.success == true) {
//                        photoUrl = response.body()?.photoUrl
//                        resultText = "Fetched photo successfully!"
//                    } else {
//                        resultText = "Fetch failed: ${response.body()?.let { it } ?: response.message()}"
//                    }
//                } catch (e: Exception) {
//                    resultText = "Error: ${e.message}"
//                }
//            }
//        }) {
//            Text("Fetch Photo")
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        photoUrl?.let {
//            Image(
//                painter = rememberAsyncImagePainter(it),
//                contentDescription = "Profile Photo",
//                modifier = Modifier
//                    .size(120.dp)
//                    .padding(8.dp)
//                    .clip(CircleShape)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Result: $resultText")
//    }
//}
