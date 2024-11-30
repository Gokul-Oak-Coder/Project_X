package com.example.projectx.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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

        binding.username.text = intent.getStringExtra("user")

        binding.langTrans.setOnClickListener {
            showLanguageSelectionDialog()
        }

        val nightMode = PreferenceUtil.loadNightMode(this)

// Set initial state for the switch
        binding.switchMode.isChecked = nightMode

// Set listener for theme toggle
        binding.switchMode.setOnClickListener {
            if (nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                PreferenceUtil.saveNightMode(this, false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                PreferenceUtil.saveNightMode(this, true)
            }
        }

    }
    private fun showLanguageSelectionDialog(){
        val languages = arrayOf("English", "தமிழ் (Tamil)","മലയാളം (Malayalam)", "తెలుగు (Telugu)","ಕನ್ನಡ (Kannada)","हिन्दी (Hindi)","Español (Spanish)")
        val localCodes = arrayOf("en","ta", "ml", "te", "kn", "hi", "es")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Language")
        builder.setItems(languages){ dialog, which ->
            val selectedLanguageCode = localCodes[which]
            changeLocale(selectedLanguageCode)
        }
        builder.show()
    }

    private fun changeLocale(languageCode : String){
        ViewUtils.setLocale(this, languageCode)

        recreate()
    }
}
