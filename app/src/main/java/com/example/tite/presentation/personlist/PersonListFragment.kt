package com.example.tite.presentation.personlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tite.R
import com.example.tite.databinding.FragmentPersonListBinding
import com.example.tite.presentation.MainActivity
import com.example.tite.presentation.messagelist.MessageListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonListFragment : Fragment(R.layout.fragment_person_list) {

    private var binding: FragmentPersonListBinding? = null
    private var adapter: PersonListAdapter? = null
    private val viewModel: PersonListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initRecyclerView()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.personList.collect {
                adapter?.items = it
            }
        }
    }

    private fun initRecyclerView() {
        adapter = PersonListAdapter { personName ->
            navigateToDetail(personName)
        }
        binding?.let { it.personListRecyclerView.adapter = adapter }
    }

    private fun navigateToDetail(personName: String) {
        findNavController().navigate(
            PersonListFragmentDirections
                .actionPersonListFragmentToMessageListFragment(personName)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initBinding(view: View) {
        binding = FragmentPersonListBinding.bind(view)
    }
}