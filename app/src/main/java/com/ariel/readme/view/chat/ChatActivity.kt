package com.ariel.readme.view.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.ariel.readme.R
import com.ariel.readme.services.AuthService
import com.ariel.readme.view.sender.SenderFragment

import com.ariel.readme.databinding.ActivityChatBinding


class ChatActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityChatBinding

    companion object{
        val ARG_CHAT_ID = "chatId"
        val ARG_DISPLAY_NAME = "displayName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChatBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)

        val extras = intent.extras
        val displayName = extras!!.getString(ARG_DISPLAY_NAME, "")
        _binding.toolbar.title = "Chat with - $displayName"

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val bund = Bundle()
            bund.putString(MessageListFragment.ARG_BUNDLE_CHAT_ID, extras!!.getString(ARG_CHAT_ID))
            bund.putString("userId", AuthService.getCurrentFirebaseUser()!!.uid)
            add<MessageListFragment>(R.id.fragmentContainerView5, null ,bund)
            replace(R.id.fragmentContainerView6, SenderFragment::class.java, bund, null)
        }
    }
}