package com.example.projectx.requests

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val username : String,
    val firstName : String,
    val lastName : String,
    @SerializedName("device_id")
    val deviceId : String,
    val email : String,
    val password : String

)