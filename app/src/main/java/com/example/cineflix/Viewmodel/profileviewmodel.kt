package com.example.cineflix.Viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineflix.R
import com.example.cineflix.Screen.Profile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _profiles = mutableStateListOf<Profile>()
    val profiles: List<Profile> = _profiles

    fun loadProfiles(userId: String) {
        firestore.collection("users").document(userId)
            .collection("profiles")
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                _profiles.clear()
                snapshot?.forEach { doc ->
                    val profile = Profile(
                        name = doc.getString("name") ?: "Unnamed",
                        imageUrl = doc.getString("imageUrl"),
                        imageRes = R.drawable.profileicon
                    )
                    _profiles.add(profile)
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

}
