package com.ariel.readme.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.ariel.readme.R
import com.ariel.readme.services.AuthService
import com.ariel.readme.voice.SenderFragment

class ChatActivity : AppCompatActivity() {
    companion object{
        val ARG_CHAT_ID = "chatId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title = "Chat with +972-584567898"

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val bund = Bundle()
            val extras = intent.extras

            bund.putString(MessageListFragment.ARG_BUNDLE_CHAT_ID, extras!!.getString(ARG_CHAT_ID))
            bund.putString("userId", AuthService.getCurrentFirebaseUser()!!.uid)
            add<MessageListFragment>(R.id.fragmentContainerView5, null ,bund)
            replace(R.id.fragmentContainerView6, SenderFragment::class.java, bund, null)

        }
    }
}