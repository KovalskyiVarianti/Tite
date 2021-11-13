package com.example.tite.presentation.persontocontact

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tite.R
import com.example.tite.databinding.FragmentPersonToContactBinding
import com.example.tite.presentation.extensions.loadAvatar
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonToContactFragment : Fragment(R.layout.fragment_person_to_contact) {

    private var binding: FragmentPersonToContactBinding? = null
    private val viewModel: PersonToContactViewModel by viewModel()
    private val navArgs by navArgs<PersonToContactFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initViewModel()
        initButton()
    }

    private fun initButton() {
        binding?.apply {
            addToContactsButton.setOnClickListener {
                val relationText = relationEditText.text.toString()
                if (relationText.isNotBlank()) {
                    lifecycleScope.launchWhenCreated {
                        viewModel.personInfo.collect { person ->
                            viewModel.addToContactAndCreateChat(person, relationText) { chatId ->
                                findNavController().navigate(
                                    PersonToContactFragmentDirections
                                        .actionPersonToContactFragmentToMessageListFragment(
                                            chatId,
                                            person.uid
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removePersonInfoListener(navArgs.personUID)
    }

    private fun initViewModel() {
        lifecycleScope.launchWhenCreated {
            viewModel.addPersonInfoListener(navArgs.personUID)
            viewModel.personInfo.collect { person ->
                binding?.apply {
                    personAvatar.loadAvatar(person.photo)
                    personName.text = person.name
                    personEmail.text = person.email
                }
            }
        }
    }

    private fun initBinding(view: View) {
        binding = FragmentPersonToContactBinding.bind(view)
    }
}