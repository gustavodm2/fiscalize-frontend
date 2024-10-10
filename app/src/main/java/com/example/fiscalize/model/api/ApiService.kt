package com.example.fiscalize.model.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Response

interface ApiService {
    @Multipart
    @POST("/image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>
}
