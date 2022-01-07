package com.ariel.readme.view.hotwords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariel.readme.databinding.FragmentHotWordBinding
import com.ariel.readme.services.AuthService

class HotWordFragment : Fragment() {

    private var _binding: FragmentHotWordBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HotWordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val uid : String = AuthService.getCurrentFirebaseUser()!!.uid
        viewModel = ViewModelProvider(this).get(HotWordViewModel::class.java)
        _binding = FragmentHotWordBinding.inflate(inflater, container, false)
        viewModel.listenOnHotWords(uid)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loading()
        updateView()
    }

    private fun updateView(){
        viewModel.hotWords.observe(viewLifecycleOwner, Observer { item ->
            binding.wordList.adapter = HotWordsRecyclerAdapter(item!!)
            binding.wordList.layoutManager = LinearLayoutManager(this.context)
        })
    }

    private fun loading(){
        viewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading) {
                binding.wordList.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.wordList.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}