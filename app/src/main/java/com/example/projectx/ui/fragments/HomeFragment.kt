package com.example.projectx.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat.recreate
import com.example.projectx.R
import com.example.projectx.ui.activity.LogInActivity
import com.example.projectx.util.PreferenceUtil
import com.example.projectx.util.ViewUtils


class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var username : TextView = view.findViewById(R.id.username)
      // username.text = activity?.intent?.getStringExtra("user")

    }
}