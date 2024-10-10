package com.example.fiscalize.model.documents

data class SimplesModel(
    val id: Int,
    val cnpj: String,
    val name: String,
    val maturityDate: String,
    val calculationPeriod: String,
    val documentNumber: String,
    val observations: List<Tax> = emptyList()
)


