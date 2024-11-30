package com.example.projectx.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.projectx.R
import com.example.projectx.databinding.ActivityMainBinding
import com.example.projectx.databinding.ActivitySignUpBinding
import com.example.projectx.network.NetworkHelper
import com.example.projectx.network.Resource
import com.example.projectx.network.RetrofitInstance
import com.example.projectx.repository.LoginRepository
import com.example.projectx.repository.RegisterRepository
import com.example.projectx.requests.LoginRequest
import com.example.projectx.requests.RegisterRequest
import com.example.projectx.util.ViewUtils.Companion.snackBar
import com.example.projectx.util.ViewUtils.Companion.startActivity
import com.example.projectx.util.ViewUtils.Companion.startNewActivity
import com.example.projectx.util.ViewUtils.Companion.toast
import com.example.projectx.viewmodel.LoginViewModel
import com.example.projectx.viewmodel.LoginViewModelFactory
import com.example.projectx.viewmodel.RegisterViewModel
import com.example.projectx.viewmodel.RegisterViewModelFactory
import com.google.android.material.snackbar.Snackbar

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val networkHelper = NetworkHelper(this)

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(networkHelper,RegisterRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerBtn.setOnClickListener {
            val username = binding.usernameEdt.text.toString()
            val firstName = binding.firstnameEdt.text.toString()
            val lastName = binding.lastnameEdt.text.toString()
            val email = binding.emailEdt.text.toString()
            val password = binding.passEdt.text.toString()

            if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                this.toast(getString(R.string.fields_empty_error))
            } else {

                val user = RegisterRequest(
                    username = username,
                    firstName = firstName,
                    lastName = lastName,
                    deviceId = "1", // Static for now, can be dynamic
                    email = email,
                    password = password
                )

                registerViewModel.registerUser(user).observe(this, Observer { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Show loading spinner
                            binding.loadingProgressBar.visibility = View.VISIBLE
                            binding.registerBtn.isEnabled = false
                        }

                        is Resource.Success -> {
                            binding.loadingProgressBar.visibility = View.GONE
                            binding.registerBtn.isEnabled = true
                            if (resource.data?.status == 200) {
                                this.toast("Login Successful: ${resource.data.message}")
                                startActivity(LogInActivity::class.java)
                                finish()
                            }
                            // Hide loading spinner and show success message
                            this.toast(resource.data?.message ?: "")

                        }

                        is Resource.Error -> {
                            // Hide loading spinner and show error message
                            binding.loadingProgressBar.visibility = View.GONE
                            binding.registerBtn.isEnabled = true
                            if(resource.message == "No Internet connection") {
                                resource.message.let { showSnackbar(it) }
                            }else{
                                this.toast("Error: ${resource.message}")
                            }
                        }

                        else -> {}
                    }
                })
            }
        }

        binding.loginAccount.setOnClickListener{
            startActivity(LogInActivity::class.java)
            finish()
        }
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).also {snackbar ->
            snackbar.setAction("ok"){
                snackbar.dismiss()
            }

        }.show()
    }
}