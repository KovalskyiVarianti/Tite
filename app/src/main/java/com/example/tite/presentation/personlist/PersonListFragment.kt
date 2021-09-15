package com.example.tite.presentation.personlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tite.R
import com.example.tite.databinding.FragmentPersonListBinding

class PersonListFragment : Fragment(R.layout.fragment_person_list) {

    private var binding: FragmentPersonListBinding? = null
    private var adapter: PersonListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initRecyclerView()
        val itemList: MutableList<PersonListItem> = mutableListOf()
        for (i in 0..100) {
            itemList.add(PersonListItem.PersonItem("Name$i", "", "$i"))
        }
        adapter?.items = itemList
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