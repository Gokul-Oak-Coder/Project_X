package com.example.projectx.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projectx.R
import com.example.projectx.databinding.ActivityLogInBinding
import com.example.projectx.network.NetworkHelper
import com.example.projectx.network.Resource
import com.example.projectx.repository.AuthRepository
import com.example.projectx.requests.LoginRequest
import com.example.projectx.util.PreferenceUtil
import com.example.projectx.util.ViewUtils.Companion.showCustomToast
import com.example.projectx.util.ViewUtils.Companion.startActivity
import com.example.projectx.util.ViewUtils.Companion.toast
import com.example.projectx.viewmodel.AuthViewModel
import com.example.projectx.viewmodel.AuthViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding

    private val networkHelper = NetworkHelper(this)
    private lateinit var authViewModel: AuthViewModel
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        getLoginObserverCall()


        binding.loginBtn.setOnClickListener {
            username = binding.usernameEdt.text.toString()
            val password = binding.passEdt.text.toString()
            binding.usernameEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    binding.userLayout.error = null
                }
            })
            binding.passEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    binding.passLayout.error = null
                }
            })
            if (username.isEmpty() && password.isEmpty()) {
                this.toast(getString(R.string.fields_empty_error))
                binding.userLayout.error = getString(R.string.username_empty_error)
                binding.passLayout.error = getString(R.string.password_empty_error)
            } else if (password.isEmpty()) {
                this.toast(getString(R.string.fields_empty_error))
                binding.passLayout.error = getString(R.string.password_empty_error)
            } else if (username.isEmpty()) {
                this.toast(getString(R.string.fields_empty_error))
                binding.userLayout.error = getString(R.string.username_empty_error)
            } else {
                loginRequest(username, password)
            }
        }
        binding.signupAccount.setOnClickListener {
            startActivity(RegisterActivity::class.java)
            finish()
        }

        binding.resetPassword.setOnClickListener {
            startActivity(PasswordResetActivity::class.java)
        }
    }
    private fun initViewModel() {
        val authRepository = AuthRepository()
        val viewModelProviderFactory = AuthViewModelFactory(authRepository, networkHelper)
        authViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    private fun loginRequest(username: String, password: String) {
        val loginRequest = LoginRequest(
            password,
            username
        )
        authViewModel.login(loginRequest)
    }

    private fun getLoginObserverCall() {
        authViewModel.login.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading spinner
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.loginBtn.isEnabled = false
                }

                is Resource.Success -> {
                    // Hide loading spinner and show success message
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.loginBtn.isEnabled = true
                    onLoginSuccess()
                    this.showCustomToast("${resource.data?.message}", R.drawable.success, R.color.success_green)
                }

                is Resource.Error -> {
                    // Hide loading spinner and show error message
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.loginBtn.isEnabled = true
                    this.showCustomToast("${resource.message}", R.drawable.error, R.color.error_red)
                   // this.toast("${resource.message}")


                }

                else -> {}
            }
        })
    }
    private fun onLoginSuccess() {
        // Set the user as logged in
        PreferenceUtil.setLoggedIn(this, true)

        this.startActivity(MainActivity::class.java)
        intent.putExtra("user", username)
        finish()
    }
}