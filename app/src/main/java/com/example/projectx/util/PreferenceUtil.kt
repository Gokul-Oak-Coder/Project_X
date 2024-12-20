package com.example.projectx.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.example.projectx.util.Constants.Companion.IS_LOGGED_IN
import com.example.projectx.util.Constants.Companion.KEY_LANGUAGE
import com.example.projectx.util.Constants.Companion.PREF_NAME
import com.example.projectx.util.Constants.Companion.PROFILE_IMAGE
import com.example.projectx.util.ViewUtils.Companion.decodeBase64ToBitmap

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

        //save the login status
        fun setLoggedIn(context: Context, isLoggedIn: Boolean){
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
            editor.apply()
        }

        fun isLoggedIn(context: Context): Boolean {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(IS_LOGGED_IN, false)

        }

        fun saveProfileImage(context: Context, encodedImage: String){
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(PROFILE_IMAGE, encodedImage)
            editor.apply()
        }

        fun loadProfileImage(context: Context): Bitmap?{
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val encodedImage = sharedPreferences.getString(PROFILE_IMAGE, null)
            return if(encodedImage != null){
                decodeBase64ToBitmap(encodedImage)
            } else {
                null
            }
        }
    }
}