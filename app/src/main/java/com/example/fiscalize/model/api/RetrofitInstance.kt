package com.example.fiscalize.model.api

import android.content.Context
import com.example.fiscalize.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = BuildConfig.SERVER_IP

    fun getApiService(context: Context): ApiService {
        // Crie o SessionManager para obter o token JWT
        val sessionManager = SessionManager(context)

        // Adicione o AuthInterceptor ao OkHttpClient
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL) // substitua pela URL da sua API
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
