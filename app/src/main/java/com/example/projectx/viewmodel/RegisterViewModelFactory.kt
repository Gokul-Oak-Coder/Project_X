package com.example.projectx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectx.network.NetworkHelper
import com.example.projectx.repository.RegisterRepository

class RegisterViewModelFactory(private val networkHelper: NetworkHelper, private val registerRepository: RegisterRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(networkHelper, registerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

