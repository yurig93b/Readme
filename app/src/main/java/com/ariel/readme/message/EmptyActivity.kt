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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val bund = Bundle()
            bund.putString(MessageListFragment.ARG_BUNDLE_CHAT_ID, "k8KMmQLiGtlJiUSDDAjl")
            bund.putString("userId", "2jp4oKeG1kHZzZ3DrDaA")

            add<MessageListFragment>(R.id.fragmentContainerView2, null ,bund)
            add<VoiceRecorderFragment>(R.id.fragmentContainerView2, null ,bund)

        }
    }
}