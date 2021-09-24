package com.example.tite.presentation.chatlist

import androidx.recyclerview.widget.DiffUtil
import com.example.tite.databinding.ChatItemBinding
import com.example.tite.presentation.ChatClickListener
import com.example.tite.presentation.personlist.PersonListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

class ChatListAdapter(private val chatClickListener: ChatClickListener) :
    AsyncListDifferDelegationAdapter<ChatListItem>(ChatListDiffCallback) {

    init {
        delegatesManager.addDelegate(chatDelegate())
    }

    private fun chatDelegate() =
        adapterDelegateViewBinding<ChatListItem.ChatItem, ChatListItem, ChatItemBinding>(
            { layoutInflater, parent -> ChatItemBinding.inflate(layoutInflater, parent, false) }
        ) {
            bind {
                binding.apply {
                    personName.text = item.personName
                    personImage //TODO load image
                    chatTopMessage.text = item.topMessage
                    root.setOnClickListener { this@ChatListAdapter.chatClickListener(item.id, item.personUID) }
                }
            }
        }

    companion object ChatListDiffCallback : DiffUtil.ItemCallback<ChatListItem>() {
        override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
            return when {
                oldItem is ChatListItem.ChatItem && newItem is ChatListItem.ChatItem -> {
                    oldItem.id == newItem.id
                }
                else -> {
                    oldItem === newItem
                }
            }
        }

        override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
            return oldItem == newItem
        }
    }
}