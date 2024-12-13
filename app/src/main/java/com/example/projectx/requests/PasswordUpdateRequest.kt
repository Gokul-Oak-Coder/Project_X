package com.example.projectx.requests

data class PasswordUpdateRequest(
    val password: String,
    val token: String
)