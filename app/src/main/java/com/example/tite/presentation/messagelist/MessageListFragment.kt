package com.example.tite.presentation.messagelist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.tite.R
import com.example.tite.databinding.FragmentMessageListBinding
import com.example.tite.presentation.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageListFragment : Fragment(R.layout.fragment_message_list) {

    private val navArgs by navArgs<MessageListFragmentArgs>()
    private var binding: FragmentMessageListBinding? = null
    private val viewModel : MessageListViewModel by viewModel()
    private var adapter: MessageListAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initRecyclerView()
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