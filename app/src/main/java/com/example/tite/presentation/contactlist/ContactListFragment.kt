package com.example.tite.presentation.contactlist

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.tite.R
import com.example.tite.databinding.FragmentContactListBinding
import com.example.tite.presentation.extensions.addChip
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import timber.log.Timber

private const val KEY_RELATION_SHARED_PREFERENCE = "KEY_RELATION_SHARED_PREFERENCE"

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    private var binding: FragmentContactListBinding? = null
    private val viewModel: ContactListViewModel by inject()
    private var contactListAdapter: ContactListAdapter? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initSharedPreferences()
        initRecyclerView()
        initViewModel()
    }

    //TODO refactoring
    private fun initViewModel() = lifecycleScope.launchWhenCreated {
        viewModel.contactList.collect { list ->
            val checkedRelation = readCheckedChipRelation()
            contactListAdapter?.items = if (checkedRelation != null) {
                list.filter { it.relation == readCheckedChipRelation() }
            } else {
                list
            }
            binding?.apply {
                relationChips.fillDistinctChipGroup(list)
                relationChips.setOnCheckedChangeListener { group, checkedChipId ->
                    if (group.checkedChipIds.isEmpty()) {
                        contactListAdapter?.items = list
                        clearCheckedChipRelation()
                        return@setOnCheckedChangeListener
                    }
                    val checkedChip = group.findViewById<Chip>(checkedChipId)
                    checkedChip.saveCheckedChipRelation()
                    val text = checkedChip.text.toString()
                    contactListAdapter?.items = list.filter { it.relation == text }
                    Timber.d("Relation checked: $text ")
                }
            }
        }
    }

    private fun ChipGroup.fillDistinctChipGroup(list: List<ContactListItem.ContactItem>) {
        list.map { it.relation }.distinct().forEach { relation ->
            addChip(relation)
        }
        readCheckedChipRelation()?.let { relation ->
            forEach { view ->
                val chip = view as Chip
                if (chip.text == relation) {
                    check(chip.id)
                }
            }
        }
    }

    private fun initRecyclerView() {
        contactListAdapter = ContactListAdapter { chatId, personUID ->
            findNavController().navigate(
                ContactListFragmentDirections.actionContactListFragmentToMessageListFragment(
                    chatId,
                    personUID
                )
            )
        }
        binding?.apply {
            contactListRecyclerView.adapter = contactListAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        sharedPreferences = null
    }

    private fun initBinding(view: View) {
        binding = FragmentContactListBinding.bind(view)
    }

    private fun initSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private fun Chip.saveCheckedChipRelation() {
        sharedPreferences?.edit()?.apply {
            putString(KEY_RELATION_SHARED_PREFERENCE, text.toString())
            apply()
        }
    }

    private fun clearCheckedChipRelation() {
        sharedPreferences?.edit()?.apply {
            this.putString(KEY_RELATION_SHARED_PREFERENCE, null)
            apply()
        }
    }

    private fun readCheckedChipRelation(): String? {
        return sharedPreferences?.getString(KEY_RELATION_SHARED_PREFERENCE, null)
    }

}