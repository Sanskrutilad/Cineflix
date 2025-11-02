package com.example.cineflix.Retrofit

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Body
import retrofit2.http.Query

data class MovieResponse(
    @SerializedName("Title")
    val title: String,

    @SerializedName("Year")
    val year: String,

    @SerializedName("Rated")
    val rating: String,

    @SerializedName("Runtime")
    val duration: String,

    @SerializedName("Language")
    val languageString: String,

    @SerializedName("Plot")
    val description: String,

    @SerializedName("Actors")
    val castString: String,

    @SerializedName("Director")
    val director: String,

    @SerializedName("Writer")
    val WriterString: String,

    @SerializedName("Genre")
    val GenreString: String,

    @SerializedName("Poster")
    val Poster: String = "",

    @SerializedName("Response")
    val Response: String = "",

    @SerializedName("imdbID")
    val Imdbid: String = ""

) {

    val languages: List<String>
        get() = languageString.split(",").map { it.trim() }

    val cast: List<String>
        get() = castString.split(",").map { it.trim() }

    val Writer: List<String>
        get() = WriterString.split(",").map { it.trim() }

    val Genre: List<String>
        get() = GenreString.split(",").map { it.trim() }

}

// UploadProfileResponse.kt
data class UploadProfileResponse(
    val success: Boolean,
    val profileUrl: String?,
    val message: String?
)

// GetProfileResponse.kt


data class UserData(
    val userId: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?
)


data class LikeRequest(
    val imdbId: String,
    val userId: String
)

data class WatchRequest(
    val imdbId: String,
    val userId: String
)

data class LikedMovie(
    val imdbId: String,
    val userId: String
)

data class WatchedMovie(
    val imdbId: String,
    val userId: String,
    val watchedAt: Long
)

data class YouTubeSearchResponse(
    val items: List<YouTubeVideoItem>
)

val youtubeApiKey = "AIzaSyA8vYqtM8ANJ8hkzOpTBf5toOrLq8IBU_E"

data class YouTubeVideoItem(
    val id: VideoId
)

data class VideoId(
    val videoId: String
)

interface YouTubeApiService {
    @GET("search")
    suspend fun searchTrailer(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 1,
        @Query("key") apiKey: String
    ): YouTubeSearchResponse
}

data class LikedMoviesResponse(
    val success: Boolean,
    val imdbIds: List<String>
)

data class GetLogoResponse(
    val success: Boolean,
    val logoUrl: String?
)

val retrofit = Retrofit.Builder()
    .baseUrl("https://www.googleapis.com/youtube/v3/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val youtubeApi = retrofit.create(YouTubeApiService::class.java)

data class GetProfileResponse(
    val success: Boolean,
    val profile: ProfileItem?
)

data class Getresponse(
    val success:Boolean,
    val profile: ProfileItem?
    )
data class ProfileItem(
    val userId: String,
    val profileId: String,
    val profileName: String,
    val isChildrenProfile: Boolean,
    val photoUrl: String?,
    val uploadedAt: String
)

interface ApiService {
    @Multipart
    @POST("/uploadLogo")
    fun uploadLogo(
        @Part logo: MultipartBody.Part,
        @Part("companyId") companyId: RequestBody
    ): Call<ResponseBody>

    @GET("getProfile/{profileId}")
    suspend fun getProfileById(
        @Path("profileId") profileId: String
    ): Response<Getresponse>
    @Multipart
    @POST("uploadProfile")
    suspend fun uploadProfile(
        @Part image: MultipartBody.Part?,
        @Part("userId") userId: RequestBody,
        @Part("isChildrenProfile") isChildrenProfile: RequestBody,
        @Part("profileName") profileName: RequestBody,
        @Part("profileId") profileId: RequestBody
    ): Response<UploadProfileResponse>
    @GET("getProfilePhoto/{userId}")
    suspend fun getProfilePhoto(
        @Path("userId") userId: String
    ): Response<GetProfileResponse>

    @GET("getProfiles/{userId}")
    suspend fun getProfiles(
        @Path("userId") userId: String
    ): Response<GetProfileResponse>   // 👈 New API for all profiles

    @GET("getLogo/{companyId}")
    suspend fun getLogo(
        @Path("companyId") companyId: String
    ): Response<GetLogoResponse>

    @POST("likedmovies")
    suspend fun likedMovie(@Body likeRequest: LikeRequest): Response<ResponseBody>

    @GET("likedmovies/{userId}")
    suspend fun getLikedMovies(@Path("userId") userId: String): Response<LikedMoviesResponse>

    @POST("recentlywatched")
    suspend fun recentlyWatched(@Body watchRequest: WatchRequest): Response<ResponseBody>

    @GET("recentlywatched/{userId}")
    suspend fun getRecentlyWatched(@Path("userId") userId: String): Response<List<WatchedMovie>>
}


interface OmdbApiService {

    @GET("/")
    suspend fun getMovieByTitle(
        @retrofit2.http.Query("t") title: String,
        @retrofit2.http.Query("apikey") apiKey: String
    ): MovieResponse

    @GET("/")
    suspend fun getMovieById(
        @retrofit2.http.Query("i") imdbId: String,
        @retrofit2.http.Query("apikey") apiKey: String
    ): MovieResponse
}

object OmdbRetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: OmdbApiService = retrofit.create(OmdbApiService::class.java)
}

fun createApiService(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(ApiService::class.java)
}

data class FreeToGame(
    val id: Int,
    val title: String,
    val thumbnail: String,
    val short_description: String,
    val game_url: String,
    val genre: String,
    val platform: String
)
interface FreeToGameApiService {
    @GET("games")
    suspend fun getGames(
        @Query("platform") platform: String = "all",
        @Query("category") category: String? = null,
        @Query("sort-by") sortBy: String = "popularity"
    ): List<FreeToGame>
}
object FreeToGameRetrofit {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.freetogame.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: FreeToGameApiService = retrofit.create(FreeToGameApiService::class.java)
}
