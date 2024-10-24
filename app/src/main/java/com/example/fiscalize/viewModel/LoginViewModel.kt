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

    suspend fun loginUser(email: String, password: String, context: Context): Boolean {
        val sessionManager: SessionManager = SessionManager(context)
        val apiClient: ApiService = RetrofitInstance.getApiService(context)
        val TAG = "login"

        return try {
            val response = apiClient.login(LoginRequest(email = email, password = password))

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    sessionManager.saveAuthToken(loginResponse.authToken)
                    sessionManager.saveUserId(loginResponse.userId)
                    Log.i(TAG, "Token salvo: ${loginResponse.userId}")
                    true
                } else {
                    Log.e(TAG, "Resposta vazia")
                    false
                }
            } else {
                Log.e(TAG, "Erro na resposta: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro na requisição: ${e.message}")
            false
        }
    }
}
