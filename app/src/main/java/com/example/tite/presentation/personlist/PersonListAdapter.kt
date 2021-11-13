package com.example.tite.presentation.personlist

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tite.databinding.PersonItemBinding
import com.example.tite.presentation.PersonClickListener
import com.example.tite.presentation.extensions.loadAvatar
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

class PersonListAdapter(private val personClickListener: PersonClickListener) :
    AsyncListDifferDelegationAdapter<PersonListItem>(PersonListDiffCallback) {

    init {
        delegatesManager.addDelegate(personDelegate())
    }

    private fun personDelegate() =
        adapterDelegateViewBinding<PersonListItem.PersonItem, PersonListItem, PersonItemBinding>(
            { layoutInflater, parent ->
                PersonItemBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            bind {
                binding.apply {
                    personImage.loadAvatar(item.personImageUri)
                    personName.text = item.personName
                    personEmail.text = item.personEmail
                    root.setOnClickListener { this@PersonListAdapter.personClickListener(item.uid) }
                }
            }
        }

    companion object PersonListDiffCallback : DiffUtil.ItemCallback<PersonListItem>() {
        override fun areItemsTheSame(oldItem: PersonListItem, newItem: PersonListItem): Boolean {
            return when {
                oldItem is PersonListItem.PersonItem && newItem is PersonListItem.PersonItem -> {
                    oldItem.uid == newItem.uid
                }
                else -> {
                    oldItem === newItem
                }
            }
        }

        override fun areContentsTheSame(
            oldItem: PersonListItem,
            newItem: PersonListItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}