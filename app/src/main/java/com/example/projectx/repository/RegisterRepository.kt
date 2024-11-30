package com.example.projectx.repository

import com.example.projectx.network.NetworkService
import com.example.projectx.network.Resource
import com.example.projectx.reponses.RegisterResponse
import com.example.projectx.requests.RegisterRequest

class RegisterRepository(private val networkService : NetworkService) {

    suspend fun registerUser(user: RegisterRequest) : Resource<RegisterResponse>{
        return try {
            val response = networkService.registerUser(user)
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