package com.example.projectx.network

import com.example.projectx.reponses.LoginResponse
import com.example.projectx.reponses.RegisterResponse
import com.example.projectx.requests.LoginRequest
import com.example.projectx.requests.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkService {

    @POST("auth/register")
    suspend fun registerUser(
        @Body user: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(
        @Body user: LoginRequest
    ): Response<LoginResponse>
}