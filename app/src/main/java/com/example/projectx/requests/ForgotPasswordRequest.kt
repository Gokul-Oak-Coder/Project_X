package com.example.projectx.requests

import com.google.gson.annotations.SerializedName

sealed class ForgotPasswordRequest{
    data class Username(@SerializedName("username")val username: String) : ForgotPasswordRequest()
    data class Email(@SerializedName("email")val email: String) : ForgotPasswordRequest()
    //val username: String

}
//data class ForgotPasswordRequest(val identifier: String)

