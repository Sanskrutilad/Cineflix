package com.example.cineflix.AI


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.ai.client.generativeai.GenerativeModel
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
        listOf(
            ChatMessage(
                "🎬 Hi! Ask me about a movie (Example: *Inception*) or ask for recommendations (Example: *Recommend action movies*)",
                false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> get() = _messages

    private val OMDB_API_KEY = "f3542b87"

    private val gemini = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyA7R39IYI91WKtXhculsOWofu-8IvloyZY"
    )

    fun sendMessage(userText: String) {
        _messages.value += ChatMessage(userText, true)

        viewModelScope.launch {
            _messages.value += ChatMessage("⌛ Processing...", false)

            val isGenreQuery = detectGenreQuery(userText.lowercase())

            val response = if (isGenreQuery) {
                fetchByGenre(userText)
            } else {
                fetchMovieDetails(userText)
            }

            _messages.value = _messages.value.dropLast(1) + ChatMessage(response, false)
        }
    }

    private fun detectGenreQuery(text: String): Boolean {
        val keys = listOf("recommend", "suggest", "movies", "genre", "best", "top", "list")
        return keys.any { text.contains(it) }
    }

    private suspend fun fetchMovieDetails(name: String): String {
        return try {
            val r = MovieApiClient.api.getMovie(name.trim(), OMDB_API_KEY)
            if (r.Title == null) return "❌ Movie not found."

            """
🎬 *${r.Title}* (${r.Year})
⭐ IMDb: ${r.imdbRating}
⏳ Duration: ${r.Runtime}
🎭 Genre: ${r.Genre}
🎬 Director: ${r.Director}
🎭 Actors: ${r.Actors}

📝 *Plot*:
${r.Plot}
            """.trimIndent()

        } catch (e: Exception) {
            "⚠️ Error fetching movie details."
        }
    }

    private suspend fun fetchByGenre(query: String): String {
        return try {
            val prompt = """
User asked: "$query"
1. Determine intended movie genre.
2. Suggest 10 movies of that genre in this format:

🎬 *Genre Name*
1. Movie (Year) – IMDb Rating
2. Movie (Year) – IMDb Rating
...
""".trimIndent()

            val res = gemini.generateContent(prompt)
            res.text ?: "⚠️ Could not generate recommendations."

        } catch (e: Exception) {
            "⚠️ Error fetching genre-based recommendations."
        }
    }
}


