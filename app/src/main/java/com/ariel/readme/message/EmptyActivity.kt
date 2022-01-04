package com.ariel.readme.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.ariel.readme.R
import com.ariel.readme.voice.VoiceRecorderFragment

class EmptyActivity : AppCompatActivity() {
    companion object {
        val ARG_CHAT_ID = "chatId"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
        var bundle :Bundle ?=intent.extras
        var themessage = bundle!!.getString("ChatId")

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val bund = Bundle()
            bund.putString(MessageListFragment.ARG_BUNDLE_CHAT_ID, savedInstanceState?.getString(
                MessageListFragment.ARG_BUNDLE_CHAT_ID)!!)
            bund.putString("userId", "NbwGIdmjRLaAaM6NUAnoTbzxngT2")

            add<MessageListFragment>(R.id.fragmentContainerView2, null ,bund)
            add<VoiceRecorderFragment>(R.id.fragmentContainerView2, null ,bund)

        }
    }
}