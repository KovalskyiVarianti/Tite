package com.example.tite.presentation.messagelist

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.tite.R
import com.example.tite.databinding.FragmentMessageListBinding
import com.example.tite.presentation.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageListFragment : Fragment(R.layout.fragment_message_list) {

    private val navArgs by navArgs<MessageListFragmentArgs>()
    private var binding: FragmentMessageListBinding? = null
    private val viewModel: MessageListViewModel by viewModel()
    private var adapter: MessageListAdapter? = null


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
        viewModel.addMessageListener(navArgs.chatId)
        viewModel.addPersonInfoListener(navArgs.personUID)
        lifecycleScope.launchWhenCreated {
            viewModel.messageItemList.collect { messageList ->
                adapter?.items = messageList
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.personInfo.collect {
                updateToolbar(it.name)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        viewModel.removeMessageListener(navArgs.chatId)
    }

    private fun updateToolbar(title: String) {
        val toolbar = (activity as MainActivity).toolbar
        toolbar?.title = title
    }

    private fun initRecyclerView() {
        val layoutManager =
            (binding?.messageListRecyclerView?.layoutManager as? LinearLayoutManager)
        adapter = MessageListAdapter()
        layoutManager?.stackFromEnd = true
        binding?.messageListRecyclerView?.adapter = adapter
    }

    private fun initBinding(view: View) {
        binding = FragmentMessageListBinding.bind(view)
    }
}