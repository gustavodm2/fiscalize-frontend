package com.example.fiscalize.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiscalize.model.api.RetrofitInstance
import com.example.fiscalize.model.documents.FilteredTaxes
import com.example.fiscalize.model.documents.SimplesModel
import com.example.fiscalize.model.documents.TaxModel
import com.example.fiscalize.ui.theme.appColors
import kotlinx.coroutines.launch


import kotlin.math.abs

class GraphViewModel : ViewModel() {

    var simplesNacional by mutableStateOf(listOf<SimplesModel>())
    var taxes by mutableStateOf(listOf<TaxModel>())
    val filteredTaxes = mutableStateListOf<FilteredTaxes>()
    val TAG = "ApiCall"

    var selectedDocument by mutableStateOf<SimplesModel?>(null)

    var selectedTax by mutableStateOf<FilteredTaxes?>(null)

    var taxListByCode by mutableStateOf(listOf<TaxModel>())

    fun getDocuments(context: Context) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.getApiService(context).getDocuments()
                if (response.isSuccessful) {
                    response.body()?.let { responseList ->
                        simplesNacional = responseList

                        taxes = simplesNacional.flatMap { it.taxes }

                        filterDocuments()
                    }
                }
            } catch (e: Exception) {
                Log.i(TAG, "onFailure: ${e.message}")
            }
        }
    }

    fun updateSelectedDocument(simplesModel: SimplesModel) {
        selectedDocument = simplesModel
    }

    fun updateSelectedTax(tax: FilteredTaxes) {
        selectedTax = tax
    }

    private fun getColorForTax(code: String): Color {
        val colors = appColors
        val index = abs(code.hashCode()) % colors.size
        return colors[index]
    }

    private fun filterDocuments() {
        filteredTaxes.clear()

        for (doc in simplesNacional) {
            for (tax in doc.taxes) {
                if (tax.code.isNotEmpty() || tax.code.isNotBlank()) {
                    val existingTaxIndex = filteredTaxes.indexOfFirst { it.code == tax.code }

                    if (existingTaxIndex != -1) {
                        val existingTax = filteredTaxes[existingTaxIndex]
                        val updatedTotal = existingTax.total + tax.total.toFloat()
                        filteredTaxes[existingTaxIndex] = existingTax.copy(total = updatedTotal)
                    } else {
                        val color = getColorForTax(tax.code)
                        filteredTaxes.add(
                            FilteredTaxes(
                                tax.code,
                                tax.total.toFloat(),
                                tax.denomination,
                                color
                            )
                        )
                    }
                }
            }
        }
    }
    fun findAllTaxesByCode(taxCode: String): List<TaxModel> {
        return taxes.filter { it.code == taxCode }
    }

}

