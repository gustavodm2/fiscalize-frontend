package com.example.fiscalize.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.fiscalize.model.api.ApiService
import com.example.fiscalize.model.api.RetrofitInstance
import com.example.fiscalize.model.api.SessionManager
import com.example.fiscalize.model.user.LoginRequest
import com.example.fiscalize.model.user.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {

    suspend fun loginUser(email: String, password: String, context: Context) {
        val sessionManager: SessionManager = SessionManager(context)
        val apiClient: ApiService = RetrofitInstance.api
        val TAG = "login"


        try {
            val response = apiClient.login(LoginRequest(email = email, password = password))

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    sessionManager.saveAuthToken(loginResponse.authToken)
                    Log.i(TAG, "Token salvo: ${loginResponse.authToken}")
                } else {
                    Log.e(TAG, "Resposta vazia")
                }
            } else {
                Log.e(TAG, "Erro na resposta: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro na requisição: ${e.message}")
        }

    }

}