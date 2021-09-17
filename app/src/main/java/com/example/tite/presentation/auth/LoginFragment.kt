package com.example.tite.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tite.R
import com.example.tite.databinding.FragmentLoginBinding
import com.example.tite.presentation.MainActivity
import com.example.tite.presentation.extensions.showSnackbar
import com.example.tite.utils.AuthUtils
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var binding: FragmentLoginBinding? = null
    private val authUtils: AuthUtils by inject()
    private val viewModel : AuthViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        binding?.initButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initBinding(view: View) {
        binding = FragmentLoginBinding.bind(view)
    }

    private fun FragmentLoginBinding.initButtons() {
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val result = authUtils.checkCredentials(email, password)
            if (result != AuthUtils.CredentialsCheckResult.Success) {
                binding?.root?.showSnackbar(result.message, Snackbar.LENGTH_LONG)
                return@setOnClickListener
            }
            login(email, password)
        }
        signUpButton.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }
    }

    private fun login(email: String, password: String) =
        authUtils.loginWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(activity, MainActivity::class.java))
            } else {
                val exception = task.exception?.message ?: "EEERORRR"
                binding?.root?.showSnackbar(exception, Snackbar.LENGTH_LONG)
            }
        }
}