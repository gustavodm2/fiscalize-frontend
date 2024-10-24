package com.example.fiscalize.model.documents

import androidx.compose.ui.graphics.Color

data class FilteredTaxes(
    val code: String,
    val total: Float,
    val denomination: String,
    val color: Color
)
