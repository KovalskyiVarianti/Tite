package com.example.tite.presentation.messagelist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tite.R
import com.example.tite.databinding.FragmentMessageListBinding
import com.example.tite.presentation.MainActivity
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageListFragment : Fragment(R.layout.fragment_message_list) {

    private val navArgs by navArgs<MessageListFragmentArgs>()
    private var binding: FragmentMessageListBinding? = null
    private val viewModel: MessageListViewModel by viewModel()
    private var messageListAdapter: MessageListAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initRecyclerView()
        initButtons()
        initViewModel()
    }

    private fun initButtons() {
        binding?.apply {
            sendButton.setOnClickListener {
                val text = messageEditText.text.toString()
                if (text.isNotBlank()) {
                    viewModel.sendMessage(navArgs.chatId, navArgs.personUID, text)
                    messageEditText.text.clear()
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel.apply {
            addMessageListener(navArgs.chatId)
            addPersonInfoListener(navArgs.personUID)
            lifecycleScope.launchWhenCreated {
                messageItemList.collect { messageList ->
                    messageListAdapter?.items = messageList
                }
            }
            lifecycleScope.launchWhenCreated {
                personInfo.collect {
                    updateToolbar(it.name)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        viewModel.removeMessageListener(navArgs.chatId)
        viewModel.removePersonInfoListener(navArgs.personUID)
    }

    private fun updateToolbar(title: String) {
        val toolbar = (activity as MainActivity).toolbar
        toolbar?.title = title
    }

    private fun initRecyclerView() {
        messageListAdapter = MessageListAdapter()
        messageListAdapter?.scrollWhenMessageAdded()
        binding?.messageListRecyclerView?.apply {
            adapter = messageListAdapter
            (layoutManager as LinearLayoutManager).stackFromEnd = true
        }
    }

    private fun initBinding(view: View) {
        binding = FragmentMessageListBinding.bind(view)
    }

    private fun MessageListAdapter.scrollWhenMessageAdded() {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding?.apply {
                    messageListRecyclerView.smoothScrollToPosition(
                        if (itemCount > 1) {
                            itemCount - 1
                        } else {
                            positionStart
                        }
                    )
                }
            }
        })
    }
}