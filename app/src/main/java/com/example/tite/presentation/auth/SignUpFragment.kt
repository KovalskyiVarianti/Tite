package com.example.tite.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tite.R
import com.example.tite.databinding.FragmentSignUpBinding
import com.example.tite.presentation.MainActivity
import com.example.tite.presentation.extensions.showSnackbar
import com.example.tite.utils.AuthUtils
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var binding: FragmentSignUpBinding? = null
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
        binding = FragmentSignUpBinding.bind(view)
    }

    private fun FragmentSignUpBinding.initButtons() {
        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val result = authUtils.checkCredentials(email, password)
            if (result != AuthUtils.CredentialsCheckResult.Success) {
                binding?.root?.showSnackbar(result.message, Snackbar.LENGTH_LONG)
                return@setOnClickListener
            }
            signUp(email, password)
        }
        loginButton.setOnClickListener {
            findNavController()
                .navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }
    }

    private fun signUp(email: String, password: String) =
        authUtils.signUpWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(activity, MainActivity::class.java))
            } else {
                val exception = task.exception?.message ?: "EEERORRR"
                binding?.root?.showSnackbar(exception, Snackbar.LENGTH_LONG)
            }
        }
}