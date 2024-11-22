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
import com.example.fiscalize.model.api.SessionManager
import com.example.fiscalize.model.documents.FilteredTaxes
import com.example.fiscalize.model.documents.SimplesModel
import com.example.fiscalize.model.documents.TaxModel
import com.example.fiscalize.ui.theme.appColors
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


import kotlin.math.abs

class GraphViewModel : ViewModel() {

    var simplesNacional by mutableStateOf(listOf<SimplesModel>())
    var taxes by mutableStateOf(listOf<TaxModel>())
    val filteredTaxes = mutableStateListOf<FilteredTaxes>()
    val TAG = "ApiCall"

    var selectedDocument by mutableStateOf<SimplesModel?>(null)

    var selectedTax by mutableStateOf<FilteredTaxes?>(null)

    var currentPage by mutableStateOf(1)
    var isLoading by mutableStateOf(false)
    var hasMorePages by mutableStateOf(true)



    fun getDocuments(context: Context, page: Int, startDate: String, endDate: String) {
        val sessionManager = SessionManager(context)
        val userId = sessionManager.fetchUserId()
        viewModelScope.launch {
            isLoading = true
            try {
                val response = userId?.let {
                    RetrofitInstance.getApiService(context).getDocumentsByUser(
                        userId = it,
                        page = page,
                        startDate = startDate,
                        endDate = endDate,
                        size = 5
                    )
                }

                if (response != null && response.isSuccessful) {
                    response.body()?.let { responseList ->
                        val uniqueDocuments = responseList.filter { it !in simplesNacional }
                        simplesNacional = simplesNacional + uniqueDocuments
                        taxes = simplesNacional.flatMap { it.taxes }
                        hasMorePages = uniqueDocuments.isNotEmpty()
                    }
                } else {
                    hasMorePages = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao carregar documentos: ${e.message}")
            } finally {
                isLoading = false
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

    fun formatDate(input: String): String {
        val inputFormat = SimpleDateFormat("MM/yyyy", Locale("pt", "BR"))
        val outputFormat = SimpleDateFormat("MMMM/yyyy", Locale("pt", "BR"))

        val date = inputFormat.parse(input)
        return outputFormat.format(date)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }


    fun getDocumentsWODate(context: Context, page: Int) {
        if (isLoading || !hasMorePages) {
            return
        }

        val sessionManager = SessionManager(context)
        val userId = sessionManager.fetchUserId()
        viewModelScope.launch {
            isLoading = true
            try {

                val response = userId?.let {
                    RetrofitInstance.getApiService(context).getDocumentsByUserWODate(userId = it, page = page, size = 10)
                }



                if (response != null) {
                    if (response.isSuccessful) {
                        Log.d("fodase", "$page")
                        response.body()?.let { responseList ->

                            val uniqueDocuments = responseList.filter { it !in simplesNacional }
                            simplesNacional += uniqueDocuments
                            taxes = simplesNacional.flatMap { it.taxes }
                            filterDocuments()

                            hasMorePages = uniqueDocuments.isNotEmpty()
                        }
                    } else {
                        hasMorePages = false
                    }
                }
            } catch (e: Exception) {
                Log.e("getDocumentsWODate", "Erro na requisição: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }



}

