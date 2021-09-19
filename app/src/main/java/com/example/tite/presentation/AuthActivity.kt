package com.example.tite.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tite.R
import com.example.tite.presentation.auth.AuthState
import com.example.tite.presentation.auth.AuthViewModel
import com.example.tite.presentation.extensions.showSnackbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() = lifecycleScope.launchWhenStarted {
        viewModel.loginState.collect { loginState ->
            when (loginState) {
                is AuthState.Success -> startActivity(
                    Intent(
                        this@AuthActivity,
                        MainActivity::class.java
                    )
                )
                is AuthState.Failure -> {
                    window.decorView.findViewById<View>(android.R.id.content)
                        .showSnackbar(loginState.message ?: "Error", Snackbar.LENGTH_LONG)
                }
                null -> {
                    setContentView(R.layout.auth_activity)
                }
            }
        }
    }

}