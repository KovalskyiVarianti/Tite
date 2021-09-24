package com.example.tite.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tite.R
import com.example.tite.databinding.AuthActivityBinding
import com.example.tite.domain.AuthState
import com.example.tite.presentation.auth.AuthViewModel
import com.example.tite.presentation.extensions.showSnackbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {

    //TODO Maybe binding ignore in future
    private var binding: AuthActivityBinding? = null
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initViewModel() = lifecycleScope.launchWhenCreated {
        viewModel.authState.collect { authState ->
            when (authState) {
                is AuthState.LoggedIn -> startActivity(
                    Intent(
                        this@AuthActivity,
                        MainActivity::class.java
                    )
                )
                is AuthState.Failure -> {
                    window.decorView.findViewById<View>(android.R.id.content)
                        .showSnackbar(authState.message, Snackbar.LENGTH_LONG)
                }
                is AuthState.LoggedOut -> {
                    setContentView(R.layout.auth_activity)
                }
            }
        }
    }

}