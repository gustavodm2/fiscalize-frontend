package com.example.fiscalize.model

data class PieChartData(
    var browserName: String?,
    var value: Float?
)

val getPieChartData = listOf(
    PieChartData("ICMS", 11.98F),
    PieChartData("INSS", 129.14F),
    PieChartData("IRPJ", 16.15F),
    PieChartData("CSLL", 15.62F),
)