package com.example.projectx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.projectx.MyApplication
import com.example.projectx.network.NetworkHelper
import com.example.projectx.network.Resource
import com.example.projectx.repository.LoginRepository
import com.example.projectx.requests.LoginRequest
import kotlinx.coroutines.Dispatchers

class LoginViewModel(
    private val networkHelper: NetworkHelper,
    private val loginRepository: LoginRepository
) : ViewModel() {

    fun loginUser(user: LoginRequest) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        if(networkHelper.isNetworkAvailable()) {
            try {
                val result = loginRepository.loginUser(user)
                emit(result)
            } catch (e: Exception) {
                emit(Resource.Error("Failed to register: ${e.message}"))
            }
        }
        else{
            emit(Resource.Error("No Internet connection"))
        }
    }
}