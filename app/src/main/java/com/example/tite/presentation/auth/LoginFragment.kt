package com.example.tite.presentation.auth

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tite.R
import com.example.tite.databinding.FragmentLoginBinding
import com.example.tite.presentation.extensions.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var binding: FragmentLoginBinding? = null
    private val viewModel: AuthViewModel by sharedViewModel()

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
            requireActivity().hideKeyboard()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.login(email, password)
        }
        signUpButton.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }
    }
}