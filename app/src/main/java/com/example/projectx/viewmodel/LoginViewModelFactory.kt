package com.example.projectx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectx.network.NetworkHelper
import com.example.projectx.repository.LoginRepository

class LoginViewModelFactory(private val networkHelper: NetworkHelper, private val loginRepository: LoginRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(networkHelper,loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}