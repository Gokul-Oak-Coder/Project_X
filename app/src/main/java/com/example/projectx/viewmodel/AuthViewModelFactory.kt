package com.example.projectx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectx.network.NetworkHelper
import com.example.projectx.repository.AuthRepository

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository, networkHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}