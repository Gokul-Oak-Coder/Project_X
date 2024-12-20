package com.example.projectx.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.projectx.R
import com.example.projectx.databinding.ActivityMainBinding
import com.example.projectx.util.PreferenceUtil
import com.example.projectx.util.ViewUtils
import com.example.projectx.util.ViewUtils.Companion.loadLocale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.navHostFragment)
        // NavigationUI.setupWithNavController(binding.bottomNavView,navController)
        binding.bottomNavView.setupWithNavController(navController)



    }
}
