package com.ariel.readme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ariel.readme.data.viewmodel.BlankViewModel
import com.ariel.readme.databinding.BlankFragmentBinding

class BlankFragment : Fragment() {
    private var _binding: BlankFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BlankViewModel

    companion object {
        fun newInstance() = BlankFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(BlankViewModel::class.java)
        _binding = BlankFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.lastUser.observe(viewLifecycleOwner, Observer { item ->
            binding.fraView.text = item!!.first_name
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}