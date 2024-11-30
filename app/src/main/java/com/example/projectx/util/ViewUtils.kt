package com.example.projectx.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import android.content.res.Configuration
import android.os.Build
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.util.*

class ViewUtils {

    companion object{
        fun Context.toast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        fun <A : Activity> Activity.startActivity(activity: Class<A>) {
            Intent(this, activity).also {
                startActivity(it)
            }
        }

        fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
            Intent(this, activity).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
        fun setLocale(context: Context, language: String) {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val config = Configuration()
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)

            PreferenceUtil.saveLanguage(context, language)
        }

        fun loadLocale(context: Context){
            val languageCode = PreferenceUtil.loadLanguage(context)
            setLocale(context, languageCode)
        }
        fun View.snackBar(message: String) {
            Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
                snackbar.setAction("Ok") {
                    snackbar.dismiss()
                }
            }.show()
        }
        fun View.snackbar(message: String, action: (() -> Unit)? = null) {
            val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
            action?.let {
                snackbar.setAction("Retry") {
                    it()
                }
            }
            snackbar.show()
        }
    }

}