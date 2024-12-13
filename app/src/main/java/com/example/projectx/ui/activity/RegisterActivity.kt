package com.example.projectx.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projectx.R
import com.example.projectx.databinding.ActivitySignUpBinding
import com.example.projectx.network.NetworkHelper
import com.example.projectx.network.Resource
import com.example.projectx.repository.AuthRepository
import com.example.projectx.requests.LoginRequest
import com.example.projectx.requests.RegisterRequest
import com.example.projectx.util.ViewUtils.Companion.startActivity
import com.example.projectx.util.ViewUtils.Companion.toast
import com.example.projectx.viewmodel.AuthViewModel
import com.example.projectx.viewmodel.AuthViewModelFactory
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val networkHelper = NetworkHelper(this)
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        getRegisterObserverCall()

        binding.registerBtn.setOnClickListener {
            val username = binding.usernameEdt.text.toString()
            val firstName = binding.firstnameEdt.text.toString()
            val lastName = binding.lastnameEdt.text.toString()
            val email = binding.emailEdt.text.toString()
            val password = binding.passEdt.text.toString()


            if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                this.toast(getString(R.string.fields_empty_error))
            } else {
                if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    registerRequest(username, password, firstName, lastName, email)
                } else {
                    this.toast("Invalid Email Address")
                }
            }
        }

        binding.loginAccount.setOnClickListener {
            startActivity(LogInActivity::class.java)
            finish()
        }
    }

    private fun initViewModel() {
        val authRepository = AuthRepository()
        val viewModelProviderFactory = AuthViewModelFactory(authRepository, networkHelper)
        authViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    private fun registerRequest(
        username: String, password: String, firstName: String, lastName: String, email: String
    ) {
        val registerRequest = RegisterRequest(
            deviceId = "1",
            email = email,
            firstName = firstName,
            lastName = lastName,
            password = password,
            username = username
        )
        authViewModel.signup(registerRequest)
    }

    private fun getRegisterObserverCall() {
        authViewModel.signup.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading spinner
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.registerBtn.isEnabled = false
                }

                is Resource.Success -> {
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.registerBtn.isEnabled = true
                    startActivity(LogInActivity::class.java)
                    finish()
                    this.toast("${resource.data?.message}")
                }

                is Resource.Error -> {
                    // Hide loading spinner and show error message
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.registerBtn.isEnabled = true
                    this.toast("${resource.message}")
                }

                else -> {}
            }
        })
    }
}