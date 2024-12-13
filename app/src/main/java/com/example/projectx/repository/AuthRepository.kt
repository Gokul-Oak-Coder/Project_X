package com.example.projectx.repository

import com.example.projectx.network.RetrofitInstance
import com.example.projectx.requests.LoginRequest
import com.example.projectx.requests.RegisterRequest
import com.example.projectx.requests.PasswordUpdateRequest

class AuthRepository {

    suspend fun login(user: LoginRequest) =
        RetrofitInstance.api.loginUser(user)

    suspend fun signup(user: RegisterRequest) =
        RetrofitInstance.api.registerUser(user)

    suspend fun forgotPassword(user : Map<String, String>) =
        RetrofitInstance.api.forgotPassword(user)

    suspend fun passwordUpdate(user : PasswordUpdateRequest) =
        RetrofitInstance.api.passwordUpdate(user)

}