package com.example.projectx.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectx.network.NetworkHelper
import com.example.projectx.network.Resource
import com.example.projectx.reponses.ForgotPasswordResponse
import com.example.projectx.reponses.LoginResponse
import com.example.projectx.reponses.PasswordUpdateResponse
import com.example.projectx.reponses.RegisterResponse
import com.example.projectx.repository.AuthRepository
import com.example.projectx.requests.LoginRequest
import com.example.projectx.requests.PasswordUpdateRequest
import com.example.projectx.requests.RegisterRequest
import kotlinx.coroutines.launch
import java.io.IOException

class AuthViewModel(
    private val repository: AuthRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private var _login : MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    private var _signup : MutableLiveData<Resource<RegisterResponse>> = MutableLiveData()
    private var _forgotPassword : MutableLiveData<Resource<ForgotPasswordResponse>> = MutableLiveData()
    private var _passwordUpdate : MutableLiveData<Resource<PasswordUpdateResponse>> = MutableLiveData()
    val login: LiveData<Resource<LoginResponse>> get() = _login
    val signup: LiveData<Resource<RegisterResponse>> get() = _signup
    val forgotPassword: LiveData<Resource<ForgotPasswordResponse>> get() = _forgotPassword
    val passwordUpdate: LiveData<Resource<PasswordUpdateResponse>> get() = _passwordUpdate

    fun login(user: LoginRequest) =
        viewModelScope.launch {
            _login.postValue(Resource.Loading())
            try {
                if (networkHelper.isNetworkAvailable()) {
                    val response = repository.login(user)
                    if (response.isSuccessful) {
                        _login.postValue(Resource.Success(response.body()!!.let {
                            LoginResponse(
                                message = it.message,
                                status = it.status
                            )
                        }))
                    } else {
                        // Handle HTTP error responses based on status codes
                        val errorMessage = when (response.code()) {
                            400 -> "Bad Request. Please check your input."
                            403 -> "Invalid credentials. Please try again."
                            404 -> "User not found. Please check your username."
                            500 -> "Server error. Please try again later."
                            else -> response.message() // Fallback to generic error message
                        }
                        _login.postValue(Resource.Error(errorMessage))
                    }
                } else {
                    _login.postValue(Resource.Error("No internet. Please turn on your internet and try."))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _login.postValue(Resource.Error("Network Failure. Please check your connection"))
                    else -> _login.postValue(Resource.Error("Unexpected error occurred"))
                }
            }

        }

    fun signup(user: RegisterRequest) =
        viewModelScope.launch {
            _signup.postValue(Resource.Loading())
            try {
                if (networkHelper.isNetworkAvailable()) {
                    val response = repository.signup(user)
                    if (response.isSuccessful) {
                        _signup.postValue(Resource.Success(response.body()!!.let {
                            RegisterResponse(
                                message = it.message,
                                status = it.status
                            )
                        }))
                    } else {
                        // Handle HTTP error responses based on status codes
                        val errorMessage = when (response.code()) {
                            404 -> "Device is not registered"
                            409 -> "User already exist"
                            500 -> "Server error. Please try again later."
                            else -> response.message() // Fallback to generic error message
                        }
                        _signup.postValue(Resource.Error(errorMessage))
                    }
                } else {
                    _signup.postValue(Resource.Error("No internet. Please turn on your internet and try."))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _signup.postValue(Resource.Error("Network Failure. Please check your connection"))
                    else -> _signup.postValue(Resource.Error("Unexpected error occurred"))
                }
            }
        }

    fun forgotPassword(user: String) =
        viewModelScope.launch {
            _forgotPassword.postValue(Resource.Loading())
            try {
                if (networkHelper.isNetworkAvailable()) {

                    val requestBody = if (user.contains("@")) {
                        mapOf("email" to user)  // Handle as email
                    } else {
                        mapOf("username" to user)  // Handle as username
                    }
                    val response = repository.forgotPassword(requestBody)
                    if (response.isSuccessful) {
                        _forgotPassword.postValue(Resource.Success(response.body()!!.let {
                            ForgotPasswordResponse(
                                message = it.message,
                                status = it.status
                            )
                        }))
                    } else {
                        // Handle HTTP error responses based on status codes
                        val errorMessage = when (response.code()) {
                            404 -> "Not Found"
                            409 -> "User not found, create new account"
                            500 -> "Server error. Please try again later."
                            else -> response.message() // Fallback to generic error message
                        }
                        _forgotPassword.postValue(Resource.Error(errorMessage))
                    }
                } else {
                    _forgotPassword.postValue(Resource.Error("No internet. Please turn on your internet and try."))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _forgotPassword.postValue(Resource.Error("Network Failure. Please check your connection"))
                    else -> _forgotPassword.postValue(Resource.Error("Unexpected error occurred"))
                }
            }

        }

    fun passwordUpdate(user: PasswordUpdateRequest) =
        viewModelScope.launch {
            _passwordUpdate.postValue(Resource.Loading())
            try {
                if (networkHelper.isNetworkAvailable()) {
                    val response = repository.passwordUpdate(user)
                    if (response.isSuccessful) {
                        _passwordUpdate.postValue(Resource.Success(response.body()!!.let {
                            PasswordUpdateResponse(
                                message = it.message
                            )
                        }))
                    }else {
                        // Handle HTTP error responses based on status codes
                        val errorMessage = when (response.code()) {
                            403 -> "Invalid token"
                            404 -> "Not Found"
                            409 -> "User not found, create new account"
                            500 -> "Server error. Please try again later."
                            else -> response.message() // Fallback to generic error message
                        }
                        _passwordUpdate.postValue(Resource.Error(errorMessage))
                    }
                } else {
                    _passwordUpdate.postValue(Resource.Error("No internet. Please turn on your internet and try."))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _passwordUpdate.postValue(Resource.Error("Network Failure. Please check your connection"))
                    else -> _passwordUpdate.postValue(Resource.Error("Unexpected error occurred"))
                }
            }

        }
}