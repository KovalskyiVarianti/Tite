package com.example.tite.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tite.R
import com.example.tite.databinding.FragmentLoginBinding
import com.example.tite.presentation.MainActivity
import com.example.tite.utils.CredentialsCheckResult
import com.example.tite.utils.checkCredentials
import com.example.tite.utils.loginWithEmailAndPassword
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var binding: FragmentLoginBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        binding?.initButtons()
        updateToolbar()
    }

    private fun updateToolbar() {
        val toolbar = (activity as MainActivity).toolbar
        toolbar?.visibility = View.GONE
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
            val result = checkCredentials(email, password)
            if (result != CredentialsCheckResult.Success) {
                Snackbar.make(binding!!.root, result.message, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            loginWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPersonListFragment())
                }
            }
        }
        signUpButton.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }
    }
}