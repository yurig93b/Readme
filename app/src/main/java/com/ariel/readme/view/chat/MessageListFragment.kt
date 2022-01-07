package com.ariel.readme.view.chat

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariel.readme.data.model.Message
import com.ariel.readme.data.repo.ModeledDocumentChange
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.databinding.FragmentMessageListBinding
import com.ariel.readme.services.AuthService
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QuerySnapshot

/**
 * A fragment representing a list of Messages.
 */
class MessageListFragment : Fragment() {
    private lateinit var viewModel: MessageListViewModel
    private val msgs: MutableList<MutableLiveData<Message>> = mutableListOf()
    private val msgsDict: MutableMap<String, MutableLiveData<Message>> = mutableMapOf()
    private lateinit var _binding: FragmentMessageListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageListBinding.inflate(inflater, container, false)

        return _binding.root
    }

    private fun err(s: String?){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
    private fun observeErrors() {
        viewModel.error.observe(viewLifecycleOwner, { e ->
            err(e.message)
        })
    }

    private fun initAdapter() {
        with(_binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MessagesViewAdapter(viewLifecycleOwner, msgs, viewModel.userInfo.value!!, AuthService.getCurrentFirebaseUser()!!.uid)
        }
    }

    private fun initListenChatMessage() {
        viewModel.listenOnChatMessages(object : IGetChangedModels<Message> {
            override fun onSuccess(
                d: List<ModeledDocumentChange<Message>>,
                raw: QuerySnapshot?
            ) {
                val prevSize = msgs.size

                // Handle changes
                d.forEach { i ->
                    when (i.changeType) {
                        DocumentChange.Type.ADDED -> {
                            val m = MutableLiveData(i.obj)
                            i.obj.mid?.let {
                                msgsDict[i.obj.mid] = m
                                msgs.add(m)
                            }
                        }
                        DocumentChange.Type.MODIFIED -> {
                            msgsDict.get(i.obj.mid)?.apply {
                                value = i.obj
                            }
                        }
                    }
                }
                _binding.list.apply {
                    if(msgs.size - prevSize >0){
                        adapter?.notifyItemRangeInserted(prevSize, msgs.size - prevSize)
                    }

                    scrollToPosition(msgs.size -1)
                }
            }
            override fun onFailure(e: Exception) {
                err(e.message)
            }
        })
    }

    private fun observerLoaded() {
        viewModel.metadataLoaded.observe(viewLifecycleOwner, { loaded ->
            if (!loaded) {
                return@observe
            }

            initAdapter()
            initListenChatMessage()
        })
    }

    private fun registerListeners() {
        observeErrors()
        observerLoaded()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MessageListViewModel::class.java)
        val chatId = arguments?.getString(ARG_BUNDLE_CHAT_ID)

        chatId?.apply {
            registerListeners()
            viewModel.loadChatInfo(this)
        }
    }

    companion object {
        val ARG_BUNDLE_CHAT_ID = "chatId"

        fun newInstance(cid: String) =
            MessageListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_BUNDLE_CHAT_ID, cid)
                }
            }
    }
}