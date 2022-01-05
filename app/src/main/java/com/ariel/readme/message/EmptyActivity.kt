package com.ariel.readme.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.ariel.readme.R
import com.ariel.readme.services.AuthService
import com.ariel.readme.voice.VoiceRecorderFragment

class EmptyActivity : AppCompatActivity() {
    companion object {
        val ARG_CHAT_ID = "chatId"
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
        val extras  = intent.extras
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val bund = Bundle()
            bund.putString(MessageListFragment.ARG_BUNDLE_CHAT_ID, extras!!.getString(ARG_CHAT_ID))
            bund.putString("userId", AuthService.getCurrentFirebaseUser()!!.uid)

            add<MessageListFragment>(R.id.fragmentContainerView2, null ,bund)
            add<VoiceRecorderFragment>(R.id.fragmentContainerView2, null ,bund)
        }
    }
}