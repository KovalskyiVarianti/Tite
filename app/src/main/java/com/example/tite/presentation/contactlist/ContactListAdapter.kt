package com.example.tite.presentation.contactlist

import androidx.recyclerview.widget.DiffUtil
import com.example.tite.databinding.ContactItemBinding
import com.example.tite.presentation.ContactClickListener
import com.example.tite.presentation.extensions.loadAvatar
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

class ContactListAdapter(
    private val contactClickListener: ContactClickListener
) :
    AsyncListDifferDelegationAdapter<ContactListItem>(ContactListDiffCallback) {

    init {
        delegatesManager.addDelegate(contactDelegate())
    }

    private fun contactDelegate() =
        adapterDelegateViewBinding<ContactListItem.ContactItem, ContactListItem, ContactItemBinding>(
            { layoutInflater, parent -> ContactItemBinding.inflate(layoutInflater, parent, false) }
        ) {
            bind {
                binding.apply {
                    contactName.text = item.contactName
                    contactImage.loadAvatar(item.contactPhoto)
                    contactEmail.text = item.contactEmail
                    contactRelation.text = item.relation
                    root.setOnClickListener {
                        this@ContactListAdapter.contactClickListener(
                            item.chatId, item.contactUID
                        )
                    }
                }
            }
        }

    companion object ContactListDiffCallback : DiffUtil.ItemCallback<ContactListItem>() {
        override fun areItemsTheSame(oldItem: ContactListItem, newItem: ContactListItem): Boolean {
            return when {
                oldItem is ContactListItem.ContactItem && newItem is ContactListItem.ContactItem -> {
                    oldItem.contactUID == newItem.contactUID
                }
                else -> {
                    oldItem === newItem
                }
            }
        }

        override fun areContentsTheSame(
            oldItem: ContactListItem,
            newItem: ContactListItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}