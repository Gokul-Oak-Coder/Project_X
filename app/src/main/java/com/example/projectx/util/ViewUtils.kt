package com.example.projectx.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.projectx.R
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.util.*
import android.util.Base64

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
        fun Context.showCustomToast(message: String, icon: Int, textColor: Int) {

            val inflater = LayoutInflater.from(this)
            val layout = inflater.inflate(R.layout.custom_toast, null)

            val textView : TextView = layout.findViewById(R.id.toast_message)
            textView.setTextColor(textColor)
            textView.text = message


            val imageView: AppCompatImageView = layout.findViewById(R.id.toast_icon)
            imageView.setImageResource(icon)

            val toast = Toast(this)
            toast.view = layout
            toast.duration = LENGTH_SHORT
            toast.show()
        }

        fun encodeImageToBase64(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        fun decodeBase64ToBitmap(base64String: String): Bitmap {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    }

}