package com.example.projectx.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.projectx.R
import com.example.projectx.ui.activity.LogInActivity
import com.example.projectx.util.Constants.Companion.HELP_LINE_NO
import com.example.projectx.util.Constants.Companion.REQUEST_CODE_HELP_LINE_CALL
import com.example.projectx.util.PreferenceUtil
import com.example.projectx.util.PreferenceUtil.Companion.loadProfileImage
import com.example.projectx.util.PreferenceUtil.Companion.saveProfileImage
import com.example.projectx.util.ViewUtils
import com.example.projectx.util.ViewUtils.Companion.encodeImageToBase64
import com.example.projectx.util.ViewUtils.Companion.toast
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var profileImageView: ShapeableImageView

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {

                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        // Convert the Uri to Bitmap
                        val bitmap = withContext(Dispatchers.IO) {
                            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                        }
                        // Encode the Bitmap to Base64
                        val encodedImage = withContext(Dispatchers.IO) {
                            encodeImageToBase64(bitmap)
                        }
                        // Save the encoded image to SharedPreferences
                        withContext(Dispatchers.IO) {
                            saveProfileImage(requireContext(), encodedImage)
                        }

                        // Load the image into the ImageView
                        Glide.with(this@ProfileFragment).load(uri)
                            .circleCrop() // Apply circular crop
                            .into(profileImageView)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImageView = view.findViewById(R.id.profileImageView)
        val langTransBtn: AppCompatImageView = view.findViewById(R.id.langTrans)
        val switchMode: Switch = view.findViewById(R.id.switchMode)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val call: AppCompatImageView = view.findViewById(R.id.help)

        val savedImage = loadProfileImage(requireContext())
        savedImage?.let {
            Glide.with(this).load(it).circleCrop().into(profileImageView)
        }
        profileImageView.setOnClickListener {
            // Open the device gallery to pick an image
            pickImage.launch("image/*")
        }

        langTransBtn.setOnClickListener {
            showLanguageSelectionDialog()
        }

        call.setOnClickListener {
            checkPermissionAndInitiateCall()
        }

        val nightMode = PreferenceUtil.loadNightMode(requireContext())

        // Set initial state for the switch
        switchMode.isChecked = nightMode

        // Set listener for theme toggle
        switchMode.setOnClickListener {
            if (nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                PreferenceUtil.saveNightMode(requireContext(), false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                PreferenceUtil.saveNightMode(requireContext(), true)
            }
        }

        btnLogout.setOnClickListener {
            showLogoutDialog()
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

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Language")
        builder.setItems(languages) { dialog, which ->
            val selectedLanguageCode = localCodes[which]
            changeLocale(selectedLanguageCode)
        }
        builder.show()
    }

    private fun changeLocale(languageCode: String) {
        ViewUtils.setLocale(requireContext(), languageCode)

        ActivityCompat.recreate(requireActivity())
    }

    private fun checkPermissionAndInitiateCall() {
        // Check if the app has permission to make a call
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initiateCall()
        } else {
            // Request the permission if not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CODE_HELP_LINE_CALL
            )
        }
    }

    private fun initiateCall() {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse(HELP_LINE_NO)
        }
        startActivity(intent)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_HELP_LINE_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateCall()
            } else {
                requireActivity().toast("Permission denied to make calls")
            }
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to logout?")
            .setCancelable(false) // Prevents dismissing the dialog if clicked outside
            .setPositiveButton("Yes") { dialog, id ->
                onLogout()
            }.setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun onLogout() {
        // Clear login status
        PreferenceUtil.setLoggedIn(requireActivity(), false)

        Intent(context, LogInActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context?.startActivity(it)
        }
    }
}