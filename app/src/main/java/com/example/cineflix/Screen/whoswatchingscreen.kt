package com.example.cineflix.Screen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.cineflix.Retrofit.ApiService
import com.example.cineflix.Viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.cineflix.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProfileScreen(
    navController: NavHostController,
    apiService: ApiService,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    var profileName by remember { mutableStateOf("") }
    var isChildrenProfile by remember { mutableStateOf(false) }
    var logoUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedLogoUrl by remember { mutableStateOf<String?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        logoUri = uri
    }

    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            TopAppBar(
                title = { Text("Add Profile", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val imageRes = if (isChildrenProfile) R.drawable.child else R.drawable.ic_launcher_foreground
                            profileViewModel.addProfile(Profile(profileName, imageRes))
                            navController.popBackStack() // Go back to WhosWatchingScreen
                        },
                        enabled = profileName.isNotBlank()
                    ) {
                        Text("Save", color = if (profileName.isNotBlank()) Color.White else Color.Gray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E1E1E))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(innerPadding)
                .padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFCC00)),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (logoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(logoUri),
                        contentDescription = "Selected Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Text("😊", fontSize = 40.sp, modifier = Modifier.align(Alignment.Center))
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable {
                            imagePicker.launch("image/*")
                        }
                        .padding(4.dp)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = profileName,
                onValueChange = { profileName = it },
                label = { Text("Profile name", color = Color.LightGray, fontSize = 12.sp) },
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF2C2C2C),
                    focusedContainerColor = Color(0xFF3C3C3C),
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Children's Profile",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Made for children 12 and under, but parents have all the control.",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Switch(
                    checked = isChildrenProfile,
                    onCheckedChange = { isChildrenProfile = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF4CAF50),
                        uncheckedThumbColor = Color.LightGray
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (logoUri != null) {
                        Log.d("Upload", "Starting upload...") // Log start
                        isUploading = true
                        coroutineScope.launch {
                            uploadLogo(
                                context = context,
                                apiService = apiService,
                                logoUri = logoUri!!,
                                userId = "your_user_id_here"
                            ) { success, imageUrl ->
                                isUploading = false
                                if (success) {
                                    uploadedLogoUrl = imageUrl
                                    Log.d("Upload", "Upload successful: $imageUrl")
                                    Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show()
                                } else {
                                    Log.e("Upload", "Upload failed.")
                                    Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Log.w("Upload", "No image selected")
                        Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = logoUri != null && !isUploading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(if (isUploading) "Uploading..." else "Upload Logo")
            }
        }
    }
}


fun uploadLogo(
    context: Context,
    apiService: ApiService,
    logoUri: Uri,
    userId: String,
    onResult: (Boolean, String?) -> Unit
) {
    val contentResolver = context.contentResolver

    try {
        val inputStream = contentResolver.openInputStream(logoUri)
        val bytes = inputStream?.readBytes()

        if (bytes == null) {
            Log.e("Upload", "Failed to read bytes from URI")
            onResult(false, null)
            return
        }

        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        val logoPart = MultipartBody.Part.createFormData("logo", "logo.png", requestBody)
        val companyIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())

        Log.d("Upload", "Initiating API call to upload logo...")

        apiService.uploadLogo(logoPart, companyIdPart).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("Upload", "Response received: ${response.code()}")
                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()
                    Log.d("Upload", "Upload successful. Response body: $bodyString")
                    onResult(true, bodyString)
                } else {
                    Log.e("Upload", "Upload failed. Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                    onResult(false, null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Upload", "Upload error: ${t.localizedMessage}", t)
                onResult(false, null)
            }
        })

    } catch (e: Exception) {
        Log.e("Upload", "Exception during upload: ${e.localizedMessage}", e)
        onResult(false, null)
    }
}

suspend fun fetchUserLogo(
    apiService: ApiService,
    companyId: String,
    onResult: (String?) -> Unit
) {
    try {
        val response = apiService.getLogo(companyId)
        if (response.isSuccessful && response.body()?.success == true) {
            onResult(response.body()?.logoUrl)
        } else {
            Log.e("fetchUserLogo", "Logo not found or failed")
            onResult(null)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onResult(null)
    }
}

fun uploadProfilePhoto(
    context: Context,
    apiService: ApiService,
    imageUri: Uri?,
    userId: String,
    isChildrenProfile: Boolean,
    profileName: String,
    profileId: String,
    onResult: (Boolean, String?) -> Unit
) {
    val contentResolver = context.contentResolver

    try {
        val imagePart = imageUri?.let { uri ->
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) {
                val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", "profile.jpg", requestBody)
            } else null
        }

        val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val isChildrenProfilePart = isChildrenProfile.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val profileNamePart = profileName.toRequestBody("text/plain".toMediaTypeOrNull())
        val profileIdPart = profileId.toRequestBody("text/plain".toMediaTypeOrNull())

        Log.d("UploadProfile", "Initiating profile upload...")

        // Since your API uses suspend function, run it inside a coroutine
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.uploadProfile(
                    image = imagePart,
                    userId = userIdPart,
                    isChildrenProfile = isChildrenProfilePart,
                    profileName = profileNamePart,
                    profileId = profileIdPart
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val result = response.body()?.toString() ?: "Success"
                        Log.d("UploadProfile", "Upload successful: $result")
                        onResult(true, result)
                    } else {
                        Log.e("UploadProfile", "Upload failed: ${response.code()}, ${response.errorBody()?.string()}")
                        onResult(false, null)
                    }
                }
            } catch (e: Exception) {
                Log.e("UploadProfile", "Exception during upload: ${e.localizedMessage}", e)
                withContext(Dispatchers.Main) {
                    onResult(false, null)
                }
            }
        }
    } catch (e: Exception) {
        Log.e("UploadProfile", "Exception setting up upload: ${e.localizedMessage}", e)
        onResult(false, null)
    }
}
