package com.example.tite.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tite.R
import com.example.tite.utils.AuthUtils
import org.koin.android.ext.android.inject

class AuthActivity : AppCompatActivity() {

    private val authUtils : AuthUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        authUtils.firebaseAuth.currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
    }
}