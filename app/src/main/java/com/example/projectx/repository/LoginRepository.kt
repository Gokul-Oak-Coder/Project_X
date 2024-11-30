package com.example.projectx.repository

import com.example.projectx.network.NetworkService
import com.example.projectx.network.Resource
import com.example.projectx.reponses.LoginResponse
import com.example.projectx.requests.LoginRequest

class LoginRepository (private val networkService : NetworkService) {

    suspend fun loginUser(user: LoginRequest): Resource<LoginResponse> {
        return try {
            val response = networkService.loginUser(user)
            if (response.isSuccessful) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Registration failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.message}")
        }

    }
}