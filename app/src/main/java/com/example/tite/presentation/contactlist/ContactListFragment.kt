package com.example.tite.presentation.contactlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tite.R
import com.example.tite.databinding.FragmentContactListBinding
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    private var binding: FragmentContactListBinding? = null
    private val viewModel: ContactListViewModel by inject()
    private var adapter: ContactListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initRecyclerView()
        initViewModel()
    }

    private fun initViewModel() = lifecycleScope.launchWhenCreated {
        viewModel.contactList.collect { list ->
            adapter?.items = list
        }
    }

    private fun initRecyclerView() {
        adapter = ContactListAdapter { chatId, personUID ->
            findNavController().navigate(
                ContactListFragmentDirections.actionContactListFragmentToMessageListFragment(
                    chatId,
                    personUID
                )
            )
        }
        binding?.contactListRecyclerView?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initBinding(view: View) {
        binding = FragmentContactListBinding.bind(view)
    }

}