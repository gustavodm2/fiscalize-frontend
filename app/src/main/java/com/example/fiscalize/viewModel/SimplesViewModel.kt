package com.example.fiscalize.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiscalize.model.api.RetrofitInstance
import com.example.fiscalize.model.documents.FilteredTaxes
import com.example.fiscalize.model.documents.SimplesModel
import kotlinx.coroutines.launch

class SimplesViewModel : ViewModel() {

    var simplesNacional by mutableStateOf(listOf<SimplesModel>())
    val filteredTaxes = mutableListOf<FilteredTaxes>()
    val TAG = "ApiCall"


     suspend fun getDocuments() {
         viewModelScope.launch {
             try {
                 val response = RetrofitInstance.api.getDocuments()
                 if (response.isSuccessful) {
                     response.body()?.let { responseList ->
                         simplesNacional = responseList
                         Log.d("simples", "${simplesNacional}")

                        filterDocuments();
                     }
                 }
             } catch (e: Exception) {
                 Log.i(TAG, "onFailure: ${e.message}")
             }
         }
     }

    var selectedDocument by mutableStateOf<SimplesModel?>(null)

    fun updateSelectedTvShow(simplesModel: SimplesModel) {
        selectedDocument = simplesModel
    }



    fun filterDocuments() {


        for (doc in simplesNacional) {
            for (tax in doc.taxes) {
                if (tax.code.isNotEmpty() || tax.code.isNotBlank()) {
                    val existingTax = filteredTaxes.find { it.code == tax.code }

                    if (existingTax != null) {
                        val updatedTotal = existingTax.total + tax.total.toFloat()
                        filteredTaxes[filteredTaxes.indexOf(existingTax)] = FilteredTaxes(tax.code, updatedTotal, tax.denomination)
                    } else {
                        filteredTaxes.add(FilteredTaxes(tax.code, tax.total.toFloat(), tax.denomination))
                    }
                }
            }
        }

        Log.d("Filtered Taxes", "$filteredTaxes")
    }








}