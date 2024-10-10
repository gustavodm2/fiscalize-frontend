package com.example.fiscalize.model

data class PieChartData(
    var browserName: String?,
    var value: Float?
)

val getPieChartData = listOf(
    PieChartData("Chrome", 34.68F),
    PieChartData("Firefox", 16.60F),
    PieChartData("Safari", 16.15F),
    PieChartData("Internet Explorer", 15.62F),
)