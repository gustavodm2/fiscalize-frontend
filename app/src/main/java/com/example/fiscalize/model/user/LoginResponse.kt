package com.example.fiscalize.model.user

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("token")
    var authToken: String,

    @SerializedName("id")
    var user: String
)