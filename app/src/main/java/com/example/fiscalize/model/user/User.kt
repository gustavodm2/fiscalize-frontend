package com.example.fiscalize.model.user

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("id")
    var id: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String,

    @SerializedName("corporateName")
    var corporateName: String,

    @SerializedName("cnpj")
    var cnpj: String,

    @SerializedName("tradeName")
    var tradeName: String,

    @SerializedName("role")
    var role: String,
)
    
