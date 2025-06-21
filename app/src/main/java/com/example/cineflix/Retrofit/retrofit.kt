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

data class PersonalData(
    val employee: String
)

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

fun createApiService(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(ApiService::class.java)
}
