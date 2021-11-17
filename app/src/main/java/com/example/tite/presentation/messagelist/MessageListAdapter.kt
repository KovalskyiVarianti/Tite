package com.example.tite.presentation.messagelist

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.setMargins
import androidx.recyclerview.widget.DiffUtil
import com.example.tite.R
import com.example.tite.databinding.MessageItemBinding
import com.example.tite.presentation.chatlist.ChatListItem
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
                    messageCardView.setParamsBySender(item.isSelf)
                    messageGuideline.setPercentBySender(item.isSelf)
                    root.setConstraintsBySender(item.isSelf)
                }
            }
        }

    private fun CardView.setParamsBySender(isSelf: Boolean) {
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            this.gravity = if (isSelf) Gravity.END else Gravity.START
            setMargins(7)
        }
    }

    private fun Guideline.setPercentBySender(isSelf: Boolean) {
        setGuidelinePercent(if (isSelf) 0.3F else 0.7F)
    }

    private fun ConstraintLayout.setConstraintsBySender(isSelf: Boolean) {
        ConstraintSet().apply {
            clone(this@setConstraintsBySender)
            connect(
                R.id.messageFrame,
                if (isSelf) ConstraintSet.START else ConstraintSet.END,
                R.id.messageGuideline,
                if (isSelf) ConstraintSet.END else ConstraintSet.START
            )
            connect(
                R.id.messageFrame,
                if (isSelf) ConstraintSet.END else ConstraintSet.START,
                R.id.messageConstraintParent,
                if (isSelf) ConstraintSet.END else ConstraintSet.START,
            )
        }.applyTo(this)
    }


    companion object MessageListDiffCallback : DiffUtil.ItemCallback<MessageListItem>() {
        override fun areItemsTheSame(
            oldItem: MessageListItem,
            newItem: MessageListItem
        ): Boolean {
            return when {
                oldItem is MessageListItem.MessageItem && newItem is MessageListItem.MessageItem -> {
                    oldItem.id == newItem.id
                }
                else -> {
                    oldItem === newItem
                }
            }
        }

        override fun areContentsTheSame(
            oldItem: MessageListItem,
            newItem: MessageListItem
        ): Boolean {
            return oldItem == newItem
        }

    }

}