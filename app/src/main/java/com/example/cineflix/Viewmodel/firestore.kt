package com.example.cineflix.Viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineflix.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    fun addMovieToMyList(profileId: String,movie: MyListMovie, onResult: (Boolean) -> Unit) {
        val userId = getUserId()
        if (userId == null) {
            Log.e("MyListViewModel", "User not logged in, cannot save movie")
            onResult(false)
            return
        }
        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(userId) .collection("profiles").document(profileId)
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
    fun removeMovieFromMyList(profileId: String,movieId: String, onResult: (Boolean) -> Unit) {
        val userId = getUserId()
        if (userId == null) {
            onResult(false)
            return
        }
        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(userId).collection("profiles").document(profileId)
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
    fun isMovieLiked(movieId: String, onResult: (Boolean) -> Unit) {
        val userId = getUserId()
        if (userId == null) {
            onResult(false)
            return
        }
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users")
                    .document(userId)
                    .collection("likedMovies")
                    .document(movieId)
                    .get()
                    .await()
                onResult(doc.exists())
            } catch (e: Exception) {
                Log.e("MyListViewModel", "Like check failed: ${e.message}", e)
                onResult(false)
            }
        }
    }
    fun toggleLikeMovie(movie: MyListMovie, isLiked: Boolean, onResult: (Boolean) -> Unit) {
        val userId = getUserId()
        if (userId == null) {
            onResult(false)
            return
        }
        viewModelScope.launch {
            try {
                val ref = firestore.collection("users")
                    .document(userId)
                    .collection("likedMovies")
                    .document(movie.imdbId)

                if (isLiked) {
                    ref.delete().await()
                } else {
                    ref.set(movie).await()
                }
                onResult(true)
            } catch (e: Exception) {
                Log.e("MyListViewModel", "Toggle like failed: ${e.message}", e)
                onResult(false)
            }
        }
    }

    fun isMovieInMyList(movieId: String, onResult: (Boolean) -> Unit) {
        val userId = getUserId()
        if (userId == null) {
            onResult(false)
            return
        }
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users")
                    .document(userId)
                    .collection("myList")
                    .document(movieId)
                    .get()
                    .await()
                onResult(doc.exists())
            } catch (e: Exception) {
                Log.e("MyListViewModel", "Check failed: ${e.message}", e)
                onResult(false)
            }
        }
    }

    fun getMyList(onResult: (List<MyListMovie>) -> Unit) {
        val userId = getUserId() ?: return onResult(emptyList())
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users")
                    .document(userId)
                    .collection("myList")
                    .get()
                    .await()
                val list = snapshot.toObjects(MyListMovie::class.java)
                onResult(list)
            } catch (e: Exception) {
                Log.e("MyListViewModel", "Failed to fetch my list: ${e.message}", e)
                onResult(emptyList())
            }
        }
    }
}
data class LikedMovie(
    val imdbId: String = "",
    val title: String = "",
    val poster: String = ""
)

class LikedMoviesViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private fun getUserId(): String? = auth.currentUser?.uid

    fun addMovieToLiked(movie: LikedMovie, onResult: (Boolean) -> Unit) {
        val userId = getUserId()
        if (userId == null) { onResult(false); return }
        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(userId)
                    .collection("likedMovies")
                    .document(movie.imdbId)
                    .set(movie)
                    .await()
                onResult(true)
            } catch (e: Exception) {
                Log.e("LikedMoviesVM", "Add failed: ${e.message}", e)
                onResult(false)
            }
        }
    }
    fun removeMovieFromLiked(movieId: String, onResult: (Boolean) -> Unit) {
        val userId = getUserId()
        if (userId == null) { onResult(false); return }
        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(userId)
                    .collection("likedMovies")
                    .document(movieId)
                    .delete()
                    .await()
                onResult(true)
            } catch (e: Exception) {
                Log.e("LikedMoviesVM", "Remove failed: ${e.message}", e)
                onResult(false)
            }
        }
    }


    fun isMovieLiked(movieId: String, onResult: (Boolean) -> Unit) {
        val userId = getUserId()
        if (userId == null) { onResult(false); return }
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users")
                    .document(userId)
                    .collection("likedMovies")
                    .document(movieId)
                    .get()
                    .await()
                onResult(doc.exists())
            } catch (e: Exception) {
                Log.e("LikedMoviesVM", "Check failed: ${e.message}", e)
                onResult(false)
            }
        }
    }

    // Get all liked movies
    fun getLikedMovies(onResult: (List<LikedMovie>) -> Unit) {
        val userId = getUserId()
        if (userId == null) { onResult(emptyList()); return }
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users")
                    .document(userId)
                    .collection("likedMovies")
                    .get()
                    .await()
                val list = snapshot.toObjects(LikedMovie::class.java)
                onResult(list)
            } catch (e: Exception) {
                Log.e("LikedMoviesVM", "Fetch failed: ${e.message}", e)
                onResult(emptyList())
            }
        }
    }
}

data class WatchedTrailer(
    val imdbId: String = "",
    val title: String = "",
    val poster: String = ""
)

class WatchedTrailersViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _watchedList = MutableStateFlow<List<WatchedTrailer>>(emptyList())
    val watchedList: StateFlow<List<WatchedTrailer>> = _watchedList

    init {
        fetchWatchedTrailers()
    }

    private fun userId(): String? = auth.currentUser?.uid

    fun addWatchedTrailer(trailer: WatchedTrailer) {
        val uid = userId() ?: return
        viewModelScope.launch {
            firestore.collection("users")
                .document(uid)
                .collection("watched")
                .document(trailer.imdbId)
                .set(trailer)
                .addOnSuccessListener {
                    fetchWatchedTrailers()
                }
        }
    }

    private fun fetchWatchedTrailers() {
        val uid = userId()
        if (uid == null) {
            Log.e("WatchedVM", "User not logged in")
            _watchedList.value = emptyList()
            return
        }

        firestore.collection("users")
            .document(uid)
            .collection("watched")
            .get()
            .addOnSuccessListener { snapshot ->
                val trailers = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(WatchedTrailer::class.java)
                    } catch (e: Exception) {
                        Log.e("WatchedVM", "Error parsing document ${doc.id}", e)
                        null
                    }
                }
                Log.d("WatchedVM", "Fetched ${trailers.size} watched trailers")
                _watchedList.value = trailers
            }
            .addOnFailureListener { e ->
                Log.e("WatchedVM", "Failed to fetch watched trailers", e)
                _watchedList.value = emptyList()
            }
    }
    fun clearHistory() {
        val uid = userId() ?: return
        firestore.collection("users")
            .document(uid)
            .collection("watched")
            .get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    doc.reference.delete()
                }
                _watchedList.value = emptyList()
            }
    }
}