package com.example.projectx.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projectx.R
import com.example.projectx.databinding.ActivityPasswordResetBinding
import com.example.projectx.network.NetworkHelper
import com.example.projectx.network.Resource
import com.example.projectx.repository.AuthRepository
import com.example.projectx.requests.ForgotPasswordRequest
import com.example.projectx.requests.LoginRequest
import com.example.projectx.requests.PasswordUpdateRequest
import com.example.projectx.util.ViewUtils.Companion.startActivity
import com.example.projectx.util.ViewUtils.Companion.toast
import com.example.projectx.viewmodel.AuthViewModel
import com.example.projectx.viewmodel.AuthViewModelFactory
import com.google.android.material.snackbar.Snackbar

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordResetBinding
    private val networkHelper = NetworkHelper(this)
    private lateinit var authViewModel: AuthViewModel
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        getForgetPasswordObserverCall()
        getPasswordUpdateObserverCall()
        handleIntent(intent)

        binding.resetPassword.setOnClickListener {
            binding.usernameEnterLayout.visibility = View.GONE
            binding.emailEnterLayout.visibility = View.VISIBLE
        }

        binding.submitBtn.setOnClickListener {
            val username = binding.usernameEdt.text.toString()
            binding.usernameEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    binding.userLayout.error = null
                }
            })
            if (username.isEmpty()) {
                this.toast(getString(R.string.fields_empty_error))
                binding.userLayout.error = getString(R.string.username_empty_error)
            } else {
               // val forgotPasswordUser = ForgotPasswordRequest.Username(username)
                forgotPasswordRequest(username)
                Log.d("passwordReset", username)
            }
        }

        binding.submitBtnEmail.setOnClickListener {
            val email = binding.emailEdt.text.toString()
            binding.emailEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    binding.emailEdt.error = null
                }
            })
            if (email.isEmpty()) {
                this.toast(getString(R.string.fields_empty_error))
                binding.emailLayout.error = getString(R.string.email_empty_error)
            } else {
                if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                   // val forgotPasswordEmail = ForgotPasswordRequest.Email(email)
                    forgotPasswordRequest(email)
                    Log.d("passwordReset", email)
                } else {
                    this.toast("Invalid Email Address")
                }
            }
        }

        binding.resetPasswordBtn.setOnClickListener {
            val password = binding.passwordEdt.text.toString()
            val confirmPassword = binding.confirmPasswordEdt.text.toString()
            binding.passwordEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    binding.passwordLayout.error = null
                }
            })
            binding.confirmPasswordEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    binding.confirmPasswordLayout.error = null
                }
            })
            if (confirmPassword.isEmpty() && password.isEmpty()) {
                this.toast(getString(R.string.fields_empty_error))
                binding.passwordLayout.error = getString(R.string.password_empty_error)
                binding.confirmPasswordLayout.error =
                    getString(R.string.confirm_password_empty_error)
            } else if (password.isEmpty()) {
                this.toast(getString(R.string.fields_empty_error))
                binding.passwordLayout.error = getString(R.string.password_empty_error)
            } else if (confirmPassword.isEmpty()) {
                this.toast(getString(R.string.fields_empty_error))
                binding.confirmPasswordLayout.error =
                    getString(R.string.confirm_password_empty_error)
            } else {
                if (password == confirmPassword) {
                    if (token != null) {
                        Log.d("PasswordReset", "$token")
                        passwordUpdateRequest(password, token!!)
                    } else this.toast("Token is null")
                } else {
                    this.toast("Password not matching each other")
                }

            }

        }
    }

    private fun initViewModel() {
        val authRepository = AuthRepository()
        val viewModelProviderFactory = AuthViewModelFactory(authRepository, networkHelper)
        authViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)

    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            binding.usernameEnterLayout.visibility = View.GONE
            binding.emailEnterLayout.visibility = View.GONE
            binding.passwordEnterLayout.visibility = View.VISIBLE
            appLinkData?.lastPathSegment?.also {
                // val data: Uri? = intent.data
                appLinkData.let {
                    // Extract the token from the URL
                    val accessToken = it.getQueryParameter("token")
                    Log.d("ResetPasswordActivity", "AccessToken: $accessToken")
                    if (accessToken != null) {
                        // Use the token to reset the password (e.g., make a network call)
                        token = accessToken
                        Log.d("ResetPasswordActivity", "Token: $token")
                    } else {
                        // Handle the case where no token is provided or invalid
                        this.toast("Invalid token")
                    }
                }

            }
        }
    }

    private fun forgotPasswordRequest(user: String) {
        /*val forgotPasswordRequest = ForgotPasswordRequest(
            username
        )*/
        Log.d("passwordReset", "in fun $user")
        authViewModel.forgotPassword(user)
    }

    private fun getForgetPasswordObserverCall() {
        authViewModel.forgotPassword.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading spinner
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.submitBtn.isEnabled = false
                }

                is Resource.Success -> {
                    // Hide loading spinner and show success message
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.submitBtn.isEnabled = false
                    this.toast("${resource.data?.message}")

                }

                is Resource.Error -> {
                    // Hide loading spinner and show error message
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.submitBtn.isEnabled = true
                    this.toast("${resource.message}")


                }

                else -> {}
            }
        })
    }

    private fun passwordUpdateRequest(password: String, token: String) {
        val passwordUpdateRequest = PasswordUpdateRequest(
            password, token
        )
        authViewModel.passwordUpdate(passwordUpdateRequest)
    }

    private fun getPasswordUpdateObserverCall() {
        authViewModel.passwordUpdate.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading spinner
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.resetPasswordBtn.isEnabled = false
                }

                is Resource.Success -> {
                    // Hide loading spinner and show success message
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.resetPasswordBtn.isEnabled = false
                    this.toast("${resource.data?.message}")
                    this.startActivity(LogInActivity::class.java)
                    finish()
                }

                is Resource.Error -> {
                    // Hide loading spinner and show error message
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.resetPasswordBtn.isEnabled = true
                    this.toast("${resource.message}")
                }

                else -> {}
            }
        })
    }
}