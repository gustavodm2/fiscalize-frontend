package com.example.fiscalize.model.api

import com.example.fiscalize.model.documents.SimplesModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("/images")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>


    @GET("/documents")
    suspend fun getDocuments() : Response<List<SimplesModel>>

}
