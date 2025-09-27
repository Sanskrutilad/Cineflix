package com.example.cineflix.Viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class MyListMovie(
    val imdbId: String = "",
    val title: String = "",
    val poster: String = ""
)

class MyListViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private fun getUserId(): String? = auth.currentUser?.uid
    fun addMovieToMyList(movie: MyListMovie, onResult: (Boolean) -> Unit) {
        val userId = getUserId()
        if (userId == null) {
            Log.e("MyListViewModel", "User not logged in, cannot save movie")
            onResult(false)
            return
        }
        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(userId)
                    .collection("myList")
                    .document(movie.imdbId)
                    .set(movie)
                    .await()
                Log.d("MyListViewModel", "Movie added successfully: ${movie.title}")
                onResult(true)
            } catch (e: Exception) {
                Log.e("MyListViewModel", "Failed to add movie: ${e.message}", e)
                onResult(false)
            }
        }
    }
    fun removeMovieFromMyList(movieId: String, onResult: (Boolean) -> Unit) {
        val userId = getUserId()
        if (userId == null) {
            onResult(false)
            return
        }
        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(userId)
                    .collection("myList")
                    .document(movieId)
                    .delete()
                    .await()

                onResult(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }
}
