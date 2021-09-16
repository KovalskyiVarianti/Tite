package com.example.tite.presentation.messagelist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.tite.R
import com.example.tite.databinding.FragmentMessageListBinding
import com.example.tite.presentation.MainActivity

class MessageListFragment : Fragment(R.layout.fragment_message_list) {

    private var binding: FragmentMessageListBinding? = null
    private var adapter: MessageListAdapter? = null
    private val navArgs by navArgs<MessageListFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initRecyclerView()
        adapter?.items = listOf(
            MessageListItem.MessageItem("Hello", true),
            MessageListItem.MessageItem("Hi", false),
            MessageListItem.MessageItem("How are you?", true),
            MessageListItem.MessageItem("Oh, really nice!", false),
        )
        updateToolbar(navArgs.personName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun updateToolbar(title: String) {
        val toolbar = (activity as MainActivity).toolbar
        toolbar?.title = title
    }

    private fun initRecyclerView() {
        adapter = MessageListAdapter()
        binding?.messageListRecyclerView?.adapter = adapter
    }

    private fun initBinding(view: View) {
        binding = FragmentMessageListBinding.bind(view)
    }
}