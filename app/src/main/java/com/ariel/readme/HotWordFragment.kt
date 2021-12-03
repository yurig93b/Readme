package com.ariel.readme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.data.repo.HotWordRepository
import com.ariel.readme.data.viewmodel.HotWordViewModel
import com.ariel.readme.databinding.FragmentHotWordBinding

class HotWordFragment : Fragment() {
    private var _binding: FragmentHotWordBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HotWordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HotWordViewModel::class.java)
        _binding = FragmentHotWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //val wordList : MutableList<String> =  mutableListOf()
        viewModel.hotWords.observe(viewLifecycleOwner, Observer { item ->
            binding.wordList.adapter = RecyclerAdapter(item!!)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}