package com.example.projectx.util

import android.content.Context
import android.content.SharedPreferences
import com.example.projectx.util.Constants.Companion.KEY_LANGUAGE
import com.example.projectx.util.Constants.Companion.PREF_NAME

class PreferenceUtil {
    companion object{

        fun saveLanguage(context: Context, languageCode : String){
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(KEY_LANGUAGE, languageCode)
            editor.apply()
        }

        fun loadLanguage(context: Context): String{
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_LANGUAGE, "en") ?: "en"

        }

        // Save night mode preference
        fun saveNightMode(context: Context, isNightMode: Boolean) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("nightMode", isNightMode)
            editor.apply()
        }

        // Load night mode preference
        fun loadNightMode(context: Context): Boolean {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("nightMode", false)
        }
    }
}