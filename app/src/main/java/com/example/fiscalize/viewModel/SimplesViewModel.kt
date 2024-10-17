package com.example.fiscalize.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiscalize.model.api.RetrofitInstance
import com.example.fiscalize.model.documents.SimplesModel
import kotlinx.coroutines.launch

class SimplesViewModel : ViewModel() {

    var simplesNacional by mutableStateOf(listOf<SimplesModel>())
    val TAG = "ApiCall"


     suspend fun getDocuments() {
         viewModelScope.launch {
             try {
                 val response = RetrofitInstance.api.getDocuments()
                 if (response.isSuccessful) {
                     response.body()?.let { responseList ->
                         simplesNacional = responseList
                     }
                 }
             } catch (e: Exception) {
                 Log.i(TAG, "onFailure: ${e.message}")
             }
         }
     }
}