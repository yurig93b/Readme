package com.ariel.readme.message

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.ariel.readme.data.model.JobStatus
import com.ariel.readme.data.model.Message
import com.ariel.readme.data.model.User
import com.ariel.readme.databinding.MessageFragmentBinding
import com.google.firebase.Timestamp
import kotlin.random.Random.Default.nextInt

class MessagesViewAdapter(
    private val viewLifeOwner: LifecycleOwner,
    private val values: List<LiveData<Message>>,
    private val userInfo: Map<String, User>,
) : RecyclerView.Adapter<MessagesViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MessageFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            userInfo
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onDataBind(values[position])
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val binding: MessageFragmentBinding, private var _userInfo: Map<String, User>?=null) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var _observer: Observer<Message>
        private var _trackedMessage: LiveData<Message>? = null

        fun fetchFullName(uid: String): String {
            _userInfo?.apply {
                 get(uid)?.apply {
                     return "$firstName $lastName"
                 }
            }
            return ""
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun formatDate(t: Timestamp?): String {
             t?.apply {
                 return SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(this.toDate())
             }
             return ""
        }

        fun initObserver(){
            _observer = object : Observer<Message> {
                @SuppressLint("SetTextI18n", "SimpleDateFormat")
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onChanged(t: Message?) {
                    t?.let {
                        binding.message.text = it.text
                        binding.sender.text = "${fetchFullName(it.from!!)} - ${formatDate(it.ts)}"

                        if (it.voice) {
                            binding.playButton.visibility = View.VISIBLE
                        }
                        if (it.voice && (it.transcriptStatus == JobStatus.PENDING || it.transcriptStatus == JobStatus.RUNNING)) {
                            binding.progressBar2.visibility = View.VISIBLE
                        } else {
                            binding.progressBar2.visibility = View.GONE
                        }
                    }
                }
            }
        }

        fun onDataBind(m: LiveData<Message>) {
            _trackedMessage?.apply {
                removeObserver(_observer)
            }
            _trackedMessage = m
            m.value?.apply {
                m.observe(viewLifeOwner, _observer)
            }
        }

        init {
            initObserver()

            val params = binding.alignMe.layoutParams as RelativeLayout.LayoutParams
            // Randomize alignment
            if (nextInt(0, 100) > 50) {
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            }
        }

    }
}