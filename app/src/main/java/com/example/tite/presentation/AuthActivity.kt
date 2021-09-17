package com.example.tite.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tite.R
import com.example.tite.utils.AuthUtils

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AuthUtils.firebaseAuth.currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
    }
}