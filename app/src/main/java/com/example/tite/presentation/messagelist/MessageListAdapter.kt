package com.example.tite.presentation.messagelist

import android.view.Gravity
import androidx.recyclerview.widget.DiffUtil
import com.example.tite.databinding.MessageItemBinding
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

class MessageListAdapter :
    AsyncListDifferDelegationAdapter<MessageListItem>(MessageListDiffCallback) {

    init {
        delegatesManager
            .addDelegate(messageDelegate())
    }

    private fun messageDelegate() =
        adapterDelegateViewBinding<MessageListItem.MessageItem, MessageListItem, MessageItemBinding>(
            { layoutInflater, parent ->
                MessageItemBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            bind {
                binding.apply {
                    messageText.text = item.messageText
                    root.gravity = if (item.isSelf) {
                         Gravity.END
                    } else {
                        Gravity.START
                    }
                }
            }
        }

    companion object MessageListDiffCallback : DiffUtil.ItemCallback<MessageListItem>() {
        override fun areItemsTheSame(oldItem: MessageListItem, newItem: MessageListItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: MessageListItem,
            newItem: MessageListItem
        ): Boolean {
            return oldItem == newItem
        }

    }

}