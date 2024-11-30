package com.example.projectx.requests

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("device_id")
    val deviceId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val username: String
)