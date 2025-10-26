package com.example.cineflix.Retrofit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineflix.Screen.Profile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val apiService: ApiService = createApiService()
) : ViewModel() {

    private val _profiles = MutableStateFlow<List<ProfileItem>>(emptyList())
    val profiles: StateFlow<List<ProfileItem>> = _profiles

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProfiles(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.getProfiles(userId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _profiles.value = response.body()?.profiles ?: emptyList()
                    Log.d("ProfileViewModel", "✅ Profiles loaded successfully: ${_profiles.value.size}")
                } else {
                    _error.value = response.message()
                    Log.e("ProfileViewModel", "❌ Failed to load profiles: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ProfileViewModel", "❌ Exception: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    fun uploadProfile(
        imagePart: okhttp3.MultipartBody.Part?,
        userId: okhttp3.RequestBody,
        isChildrenProfile: okhttp3.RequestBody,
        profileName: okhttp3.RequestBody,
        profileId: okhttp3.RequestBody
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.uploadProfile(imagePart, userId, isChildrenProfile, profileName, profileId)
                if (response.isSuccessful && response.body()?.success == true) {
                    Log.d("ProfileViewModel", "✅ Profile uploaded successfully")
                    // Refresh list after upload
                    loadProfiles(userId.toString())
                } else {
                    Log.e("ProfileViewModel", "❌ Upload failed: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "❌ Upload Exception: ${e.message}")
            }
        }
    }
    fun addProfile(userId: String, profile: Profile) {
        val firestore = FirebaseFirestore.getInstance()

        val profileData = hashMapOf(
            "profileId" to profile.profileId,
            "name" to profile.name,
            "imageUrl" to profile.imageUrl
        )

        firestore.collection("users").document(userId)
            .collection("profiles")
            .document(profile.profileId) // store with generated ID
            .set(profileData)
            .addOnSuccessListener {
                Log.d("Firestore", "Profile saved successfully ✅")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saving profile: ${e.message}")
            }
    }
    suspend fun fetchProfile(profileId: String) {
        try {
            val response = apiService.getProfileById(profileId)
            if (response.isSuccessful && response.body()?.success == true) {
                val profile = response.body()?.profile
                Log.d("ProfileViewModel", "Fetched profile: $profile")
            } else {
                Log.e("ProfileViewModel", "Failed to fetch profile: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Exception: ${e.message}")
        }
    }
}
