package com.example.fiscalize.model.documents

data class Tax(
    val code: String,
    val denomination: String,
    val principal: String,
    val fine: String,
    val fees: String,
    val total: String
)