package com.example.projectx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.projectx.network.Resource
import com.example.projectx.repository.LoginRepository
import com.example.projectx.requests.LoginRequest
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    fun loginUser(user: LoginRequest) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val result = loginRepository.loginUser(user)
            emit(result)
        } catch (e: Exception) {
            emit(Resource.Error("Failed to register: ${e.message}"))
        }
    }
}