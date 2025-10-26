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
import coil.compose.rememberAsyncImagePainter
import com.example.cineflix.Retrofit.ApiService
import com.example.cineflix.Retrofit.ProfileViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import java.io.File
import java.util.UUID

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
    var isUploading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val profileId = remember { generateProfileId() }
    var uploadedUrl by remember { mutableStateOf<String?>(null) }
    var fetchedPhotoUrl by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf("") }
    val userId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        logoUri = uri
        uri?.let {
            Log.d("Upload", "Starting upload automatically...")
            isUploading = true
            scope.launch {
                uploadLogo(
                    context = context,
                    apiService = apiService,
                    logoUri = it,
                    userId = userId
                ) { success, imageUrl ->
                    isUploading = false
                    if (success) {
                        uploadedUrl = imageUrl
                        Log.d("Upload", "Upload successful: $imageUrl")
                        Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("Upload", "Upload failed.")
                        Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
                            scope.launch {
                                logoUri?.let { uri ->
                                    try {
                                        val file = File(context.cacheDir, "temp.jpg")
                                        context.contentResolver.openInputStream(uri)?.use { input ->
                                            file.outputStream().use { output -> input.copyTo(output) }
                                        }
                                        Log.d("UploadProfile", "Temp file: ${file.absolutePath}, size=${file.length()}")
                                        val imagePart = MultipartBody.Part.createFormData(
                                            "image", file.name,
                                            RequestBody.create("image/*".toMediaTypeOrNull(), file)
                                        )

                                        val response = withContext(Dispatchers.IO) {
                                            apiService.uploadProfile(
                                                imagePart,
                                                RequestBody.create("text/plain".toMediaTypeOrNull(), userId),
                                                RequestBody.create("text/plain".toMediaTypeOrNull(), isChildrenProfile.toString()),
                                                RequestBody.create("text/plain".toMediaTypeOrNull(), profileName),
                                                RequestBody.create("text/plain".toMediaTypeOrNull(), profileId)
                                            )
                                        }

                                        Log.d("UploadProfile", "Upload response: ${response.raw()}")

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
                            val newProfile = Profile(
                                name = profileName,
                                imageRes = null,
                                imageUrl = logoUri.toString(),
                                profileId = profileId
                            )
                            profileViewModel.addProfile(
                                userId = userId,
                                profile = newProfile
                            )
                            navController.popBackStack()
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
fun generateProfileId(): String {
    return UUID.randomUUID().toString()
}