package com.example.projectx.network

import com.example.projectx.reponses.ForgotPasswordResponse
import com.example.projectx.reponses.LoginResponse
import com.example.projectx.reponses.PasswordUpdateResponse
import com.example.projectx.reponses.RegisterResponse
import com.example.projectx.requests.ForgotPasswordRequest
import com.example.projectx.requests.LoginRequest
import com.example.projectx.requests.PasswordUpdateRequest
import com.example.projectx.requests.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface NetworkService {

    @POST("auth/register")
    suspend fun registerUser(
        @Body user: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(
        @Body user: LoginRequest
    ): Response<LoginResponse>

   /* @POST("auth/forgotpassword")
    suspend fun forgotPassword(
       @Body username: ForgotPasswordRequest
    ): Response<ForgotPasswordResponse>
*/
    @POST("auth/forgotpassword")
    suspend fun forgotPassword(
        @Body user: Map<String, String>
    ): Response<ForgotPasswordResponse>

    @PUT("auth/resetpassword")
    suspend fun passwordUpdate(
        @Body user: PasswordUpdateRequest
    ): Response<PasswordUpdateResponse>
}