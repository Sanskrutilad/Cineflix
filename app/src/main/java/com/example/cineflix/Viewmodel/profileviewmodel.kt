package com.example.cineflix.Viewmodel


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.cineflix.R
import com.example.cineflix.Screen.Profile

class ProfileViewModel : ViewModel() {

    // Mutable list of profiles that both screens can observe
    private val _profiles = mutableStateListOf(
        Profile("Shrikant", R.drawable.prof),
        Profile("Sanskruti", R.drawable.profileicon),
        Profile("Children", R.drawable.child)
    )
    val profiles: List<Profile> = _profiles

    fun addProfile(profile: Profile) {
        _profiles.add(profile)
    }
}
