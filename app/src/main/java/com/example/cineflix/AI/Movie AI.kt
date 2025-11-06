package com.example.cineflix.AI

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MovieResponse(
    val Title: String?,
    val Year: String?,
    val Released: String?,
    val Runtime: String?,
    val Genre: String?,
    val Director: String?,
    val Actors: String?,
    val Plot: String?,
    val imdbRating: String?,
    val Poster: String?
)

interface MovieApiService {
    @GET("/")
    suspend fun getMovie(
        @Query("t") title: String,
        @Query("apikey") apiKey: String
    ): MovieResponse
}

object MovieApiClient {
    private const val BASE_URL = "https://www.omdbapi.com/"

    val api: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}





data class ChatMessage(val text: String, val isUser: Boolean)

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(ChatMessage("🎬 Hi! Ask me about any movie. Example: *Tell me about Inception*", false))
    )
    val messages: StateFlow<List<ChatMessage>> get() = _messages

    private val OMDB_API_KEY = "YOUR_OMDB_API_KEY"

    fun sendMessage(userText: String) {
        _messages.value += ChatMessage(userText, true)

        viewModelScope.launch {
            _messages.value += ChatMessage("⌛ Fetching movie details…", false)

            val movieName = userText.trim()
            val response = fetchMovie(movieName)

            val reply = if (response.Title != null) {
                """
 **${response.Title}** (${response.Year}
  IMDB: ${response.imdbRating}
  Released: ${response.Released}
  Duration: ${response.Runtime}
  Genre: ${response.Genre}
  Director: ${response.Director}
  Cast: ${response.Actors}

**Plot:**  
${response.Plot}
                """.trimIndent()
            } else {
                "I couldn't find this movie. Try another name."
            }

            _messages.value = _messages.value.dropLast(1) + ChatMessage(reply, false)
        }
    }

    private suspend fun fetchMovie(name: String): MovieResponse {
        return try {
            MovieApiClient.api.getMovie(name, OMDB_API_KEY)
        } catch (e: Exception) {
            MovieResponse(null, null, null, null, null, null, null, null, null, null)
        }
    }
}


@Composable
fun ChatScreen() {
    val chatViewModel: ChatViewModel = viewModel()
    val messages by chatViewModel.messages.collectAsState()

}

