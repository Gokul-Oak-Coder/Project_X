package com.example.projectx.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.projectx.R
import com.example.projectx.databinding.ActivityLogInBinding
import com.example.projectx.network.Resource
import com.example.projectx.network.RetrofitInstance
import com.example.projectx.repository.LoginRepository
import com.example.projectx.requests.LoginRequest
import com.example.projectx.util.NetworkUtils
import com.example.projectx.util.ViewUtils.Companion.startActivity
import com.example.projectx.util.ViewUtils.Companion.startNewActivity
import com.example.projectx.util.ViewUtils.Companion.toast
import com.example.projectx.viewmodel.LoginViewModel
import com.example.projectx.viewmodel.LoginViewModelFactory

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(LoginRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginBtn.setOnClickListener {
            val username = binding.usernameEdt.text.toString()
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

                val user = LoginRequest(
                    username = username,
                    password = password
                )

                loginViewModel.loginUser(user).observe(this, Observer { resource ->
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
                            this.toast("Login Successful: ${resource.data?.message}")
                            this.startActivity(MainActivity::class.java)
                            intent.putExtra("user", username)
                            finish()
                        }

                        is Resource.Error -> {
                            // Hide loading spinner and show error message
                            binding.loadingProgressBar.visibility = View.GONE
                            binding.loginBtn.isEnabled = true
                            this.toast("Error: ${resource.message}")

                        }

                        else -> {}
                    }
                })
            }
        }
        binding.signupAccount.setOnClickListener {
            startActivity(SignUpActivity::class.java)
            finish()
        }
    }
}