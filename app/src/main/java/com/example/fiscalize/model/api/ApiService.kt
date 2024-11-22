package com.example.fiscalize.model.api

import com.example.fiscalize.model.documents.SimplesModel
import com.example.fiscalize.model.user.LoginRequest
import com.example.fiscalize.model.user.LoginResponse
import com.example.fiscalize.model.user.User
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("/images")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>


    @POST("/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/users/{id}")
    suspend fun getUserData(@Path("id") id: String) : Response<User>

    @GET("/documents")
    suspend fun getDocuments() : Response<List<SimplesModel>>

    @GET("users/{id}/simplesNacional")
    suspend fun getDocumentsByUser(
        @Path("id") userId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("from") startDate: String,
        @Query("to") endDate: String

    ): Response<List<SimplesModel>>

    @GET("users/{id}/simplesNacional")
    suspend fun getDocumentsByUserWODate(
        @Path("id") userId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<List<SimplesModel>>

}
