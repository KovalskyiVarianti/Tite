package com.example.tite.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tite.R
import com.example.tite.databinding.FragmentSignUpBinding
import com.example.tite.domain.UserManager
import com.example.tite.presentation.extensions.showSnackbar
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var binding: FragmentSignUpBinding? = null
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
        binding = FragmentSignUpBinding.bind(view)
    }

    private fun FragmentSignUpBinding.initButtons() {
        signUpButton.setOnClickListener {
            val nickname = nicknameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.signUp(nickname, email, password)
        }
        loginButton.setOnClickListener {
            findNavController()
                .navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }
    }
}