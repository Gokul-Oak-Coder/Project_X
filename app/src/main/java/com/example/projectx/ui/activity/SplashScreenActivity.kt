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

    @SuppressLint("WrongThread")
    @RequiresApi(Build.VERSION_CODES.N_MR1)
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

        // Shortcut Manager for managing the shortcuts
        var shortcutManager = getSystemService(ShortcutManager::class.java)

        if (shortcutManager != null) {
            // Defining a shortcut, Shortcut 1
            var shortcut1 = ShortcutInfo.Builder(applicationContext, "ID1")
                .setShortLabel("Instagram")
                .setIcon(Icon.createWithResource(applicationContext, R.drawable.projectx))
                .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com")))
                .build()

            // Defining a shortcut, Shortcut 2
            var shortcut2 = ShortcutInfo.Builder(applicationContext, "ID2")
                .setShortLabel("AskFM")
                .setIcon(Icon.createWithResource(applicationContext, R.drawable.projectx))
                .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ask.fm")))
                .build()

            // Show list of shortcuts when held
            shortcutManager.dynamicShortcuts = listOf(shortcut1, shortcut2)
        }

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