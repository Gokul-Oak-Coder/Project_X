package com.example.projectx.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.projectx.network.Resource
import com.example.projectx.repository.RegisterRepository
import com.example.projectx.requests.RegisterRequest
import kotlinx.coroutines.Dispatchers

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    fun registerUser(user: RegisterRequest) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val result = registerRepository.registerUser(user)
            emit(result)
        } catch (e: Exception) {
            emit(Resource.Error("Failed to register: ${e.message}"))
        }
    }
}