package com.example.cineflix.Retrofit

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
    val languageString: String,  // comma-separated string from API

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

interface ApiService {

    @Multipart
    @POST("/uploadLogo")
    fun uploadLogo(
        @Part logo: MultipartBody.Part,
        @Part("companyId") companyId: RequestBody
    ): Call<ResponseBody>

    @GET("getLogo/{companyId}")
    suspend fun getLogo(@Path("companyId") companyId: String): Response<ResponseBody>
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
        .baseUrl("http://10.0.2.2:3000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(ApiService::class.java)
}
