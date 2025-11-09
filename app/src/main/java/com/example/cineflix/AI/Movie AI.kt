package com.example.cineflixai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    val chatViewModel: ChatViewModel = viewModel()
    val messages by chatViewModel.messages.collectAsState()
    var userInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(10.dp)
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("HomeScreen") }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "🎥 CineFlix AI",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                ChatBubble(message)
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        // Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                placeholder = { Text("Ask about a movie...", color = Color.Black) },
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (userInput.isNotBlank()) {
                        chatViewModel.sendMessage(userInput)
                        userInput = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Send", color = Color.White)
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser) Color(0xFF2196F3) else Color(0xFF333333)
    val textColor = Color.White
    val alignment = if (message.isUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = alignment
    ) {
        Box(
            modifier = Modifier
                .background(bubbleColor, RoundedCornerShape(12.dp))
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                fontSize = 16.sp
            )
        }
    }
}

// ----------------------
// DATA MODELS & API
// ----------------------

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

// ----------------------
// VIEWMODEL
// ----------------------

data class ChatMessage(val text: String, val isUser: Boolean)

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                "🎬 Hi! Ask me about a movie (e.g. *Inception*) or ask for recommendations (e.g. *Recommend action movies*)",
                false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> get() = _messages

    private val OMDB_API_KEY = "f3542b87" // your OMDB key

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

    // 🎯 LOCAL GENRE RECOMMENDATIONS
    private suspend fun fetchByGenre(query: String): String {
        val genre = when {
            query.contains("action") -> "Action"
            query.contains("comedy") -> "Comedy"
            query.contains("romance") || query.contains("love") -> "Romance"
            query.contains("horror") -> "Horror"
            query.contains("thriller") -> "Thriller"
            query.contains("sci-fi") || query.contains("science") -> "Sci-Fi"
            query.contains("drama") -> "Drama"
            query.contains("animation") || query.contains("cartoon") -> "Animation"
            query.contains("crime") -> "Crime"
            else -> "General"
        }

        val movies = when (genre) {
            "Action" -> listOf(
                "Inception (2010) – IMDb 8.8",
                "The Dark Knight (2008) – IMDb 9.0",
                "Mad Max: Fury Road (2015) – IMDb 8.1",
                "John Wick (2014) – IMDb 7.4",
                "Gladiator (2000) – IMDb 8.5",
                "Die Hard (1988) – IMDb 8.2",
                "The Matrix (1999) – IMDb 8.7",
                "Mission: Impossible – Fallout (2018) – IMDb 7.7",
                "Avengers: Endgame (2019) – IMDb 8.4",
                "Tenet (2020) – IMDb 7.4"
            )

            "Comedy" -> listOf(
                "The Hangover (2009) – IMDb 7.7",
                "Superbad (2007) – IMDb 7.6",
                "21 Jump Street (2012) – IMDb 7.2",
                "The Grand Budapest Hotel (2014) – IMDb 8.1",
                "Crazy Rich Asians (2018) – IMDb 6.9",
                "Jumanji: Welcome to the Jungle (2017) – IMDb 6.9",
                "Ted (2012) – IMDb 6.9",
                "Step Brothers (2008) – IMDb 6.9",
                "The Intern (2015) – IMDb 7.1",
                "Yes Man (2008) – IMDb 6.8"
            )

            "Romance" -> listOf(
                "The Notebook (2004) – IMDb 7.8",
                "La La Land (2016) – IMDb 8.0",
                "Titanic (1997) – IMDb 7.9",
                "Pride & Prejudice (2005) – IMDb 7.8",
                "500 Days of Summer (2009) – IMDb 7.7",
                "About Time (2013) – IMDb 7.8",
                "Me Before You (2016) – IMDb 7.4",
                "The Fault in Our Stars (2014) – IMDb 7.7",
                "Crazy, Stupid, Love (2011) – IMDb 7.4",
                "A Star Is Born (2018) – IMDb 7.6"
            )

            "Horror" -> listOf(
                "The Conjuring (2013) – IMDb 7.5",
                "Get Out (2017) – IMDb 7.8",
                "It (2017) – IMDb 7.3",
                "A Quiet Place (2018) – IMDb 7.5",
                "Hereditary (2018) – IMDb 7.3",
                "Insidious (2010) – IMDb 6.8",
                "The Exorcist (1973) – IMDb 8.1",
                "The Babadook (2014) – IMDb 6.8",
                "The Nun (2018) – IMDb 5.3",
                "Smile (2022) – IMDb 6.5"
            )

            "Thriller" -> listOf(
                "Se7en (1995) – IMDb 8.6",
                "Gone Girl (2014) – IMDb 8.1",
                "Shutter Island (2010) – IMDb 8.2",
                "Parasite (2019) – IMDb 8.6",
                "Prisoners (2013) – IMDb 8.1",
                "Joker (2019) – IMDb 8.4",
                "The Girl with the Dragon Tattoo (2011) – IMDb 7.8",
                "Oldboy (2003) – IMDb 8.4",
                "Nightcrawler (2014) – IMDb 7.8",
                "Memento (2000) – IMDb 8.4"
            )

            "Sci-Fi" -> listOf(
                "Interstellar (2014) – IMDb 8.6",
                "The Matrix (1999) – IMDb 8.7",
                "Blade Runner 2049 (2017) – IMDb 8.0",
                "Dune (2021) – IMDb 8.0",
                "Inception (2010) – IMDb 8.8",
                "The Martian (2015) – IMDb 8.0",
                "Ex Machina (2014) – IMDb 7.7",
                "Arrival (2016) – IMDb 7.9",
                "Edge of Tomorrow (2014) – IMDb 7.9",
                "Avatar (2009) – IMDb 7.9"
            )

            "Drama" -> listOf(
                "The Shawshank Redemption (1994) – IMDb 9.3",
                "Forrest Gump (1994) – IMDb 8.8",
                "Fight Club (1999) – IMDb 8.8",
                "Whiplash (2014) – IMDb 8.5",
                "The Pursuit of Happyness (2006) – IMDb 8.0",
                "The Green Mile (1999) – IMDb 8.6",
                "12 Angry Men (1957) – IMDb 9.0",
                "The Social Network (2010) – IMDb 7.8",
                "Joker (2019) – IMDb 8.4",
                "The Godfather (1972) – IMDb 9.2"
            )

            "Animation" -> listOf(
                "Toy Story (1995) – IMDb 8.3",
                "Up (2009) – IMDb 8.3",
                "Coco (2017) – IMDb 8.4",
                "Finding Nemo (2003) – IMDb 8.2",
                "Zootopia (2016) – IMDb 8.0",
                "Inside Out (2015) – IMDb 8.1",
                "Shrek (2001) – IMDb 7.9",
                "The Lion King (1994) – IMDb 8.5",
                "Ratatouille (2007) – IMDb 8.1",
                "Frozen (2013) – IMDb 7.4"
            )

            "Crime" -> listOf(
                "The Godfather (1972) – IMDb 9.2",
                "Pulp Fiction (1994) – IMDb 8.9",
                "The Dark Knight (2008) – IMDb 9.0",
                "The Departed (2006) – IMDb 8.5",
                "Goodfellas (1990) – IMDb 8.7",
                "Scarface (1983) – IMDb 8.3",
                "Heat (1995) – IMDb 8.3",
                "Joker (2019) – IMDb 8.4",
                "Casino (1995) – IMDb 8.2",
                "City of God (2002) – IMDb 8.6"
            )

            else -> listOf("Try asking: 'Recommend action movies' or 'Suggest romantic films'.")
        }

        return buildString {
            append("🎬 *$genre Movie Recommendations*\n\n")
            movies.forEachIndexed { index, movie ->
                append("${index + 1}. $movie\n")
            }
        }
    }
}
