package com.example.fiscalize.model.documents

data class SimplesModel(
    val _id: String,
    val CNPJ: String,
    val corporateName: String,
    val maturityDate: String,
    val calculationPeriod: String,
    val documentNumber: String,
    val observations: String,
    val taxes:List<TaxModel>
)


