package com.ariel.readme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariel.readme.data.viewmodel.SelectContactViewModel
import com.ariel.readme.databinding.FragmentChatListBinding
import com.ariel.readme.message.MessagesViewAdapter


class FragmentChatList : Fragment() {
    private lateinit var viewModel: SelectContactViewModel
    private lateinit var _binding: FragmentChatListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return _binding.root
    }

    private fun err(s: String?) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    private fun initAdapter() {
        viewModel._chat.observe(
            viewLifecycleOwner,
            Observer {
                _binding.List.adapter=Adapter(listOf(it!!))
                _binding.List.layoutManager=LinearLayoutManager(this.context)
            }
        )
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
    }

        companion object {

        }
    }
