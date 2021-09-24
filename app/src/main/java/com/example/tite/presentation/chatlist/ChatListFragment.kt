package com.example.tite.presentation.chatlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tite.R
import com.example.tite.databinding.FragmentChatListBinding
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class ChatListFragment : Fragment(R.layout.fragment_chat_list) {

    private var binding: FragmentChatListBinding? = null
    private val viewModel: ChatListViewModel by inject()
    private var adapter: ChatListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initRecyclerView()
        initViewModel()
        viewModel
    }

    private fun initViewModel() = lifecycleScope.launchWhenCreated {
        viewModel.chatItemList.collect { list ->
            adapter?.items = list
        }
    }

    private fun initRecyclerView() {
        adapter = ChatListAdapter { chatId, personUID ->
            findNavController().navigate(
                ChatListFragmentDirections.actionChatListFragmentToMessageListFragment(
                    chatId,
                    personUID
                )
            )
        }
        binding?.chatListRecyclerView?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initBinding(view: View) {
        binding = FragmentChatListBinding.bind(view)
    }
}