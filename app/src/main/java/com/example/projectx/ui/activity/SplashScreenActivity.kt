package com.example.projectx.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.projectx.R
import com.example.projectx.BuildConfig
import com.example.projectx.databinding.ActivitySplashScreenBinding
import com.example.projectx.util.ViewUtils.Companion.loadLocale
import com.example.projectx.util.ViewUtils.Companion.setLocale
import com.example.projectx.util.ViewUtils.Companion.startActivity


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        loadLocale(this)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /*Handler(Looper.getMainLooper()).postDelayed({
          //  this.startNewActivity(LogInActivity::class.java)
            startNewActivity(LogInActivity::class.java)
            finish()

        }, SPLASH_TIME)*/
        binding.getStartBtn.setOnClickListener {
            startActivity(LogInActivity::class.java)
            finish()
        }
        binding.langTrans.setOnClickListener {
            showLanguageSelectionDialog()
        }

        binding.projectName.text = resources.getText(R.string.app_name)
        val version = BuildConfig.VERSION_NAME
        binding.txtVersion.text = "v$version"

        binding.txtVersion.setOnClickListener {
            startActivity(MainActivity::class.java)
        }
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf(
            "English",
            "தமிழ் (Tamil)",
            "മലയാളം (Malayalam)",
            "తెలుగు (Telugu)",
            "ಕನ್ನಡ (Kannada)",
            "हिन्दी (Hindi)",
            "Español (Spanish)"
        )
        val localCodes = arrayOf("en", "ta", "ml", "te", "kn", "hi", "es")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Language")
        builder.setItems(languages) { dialog, which ->
            val selectedLanguageCode = localCodes[which]
            changeLocale(selectedLanguageCode)
        }
        builder.show()
    }

    private fun changeLocale(languageCode: String) {
        //   setLocale(this, languageCode)
        setLocale(this, languageCode)

        recreate()
    }

}