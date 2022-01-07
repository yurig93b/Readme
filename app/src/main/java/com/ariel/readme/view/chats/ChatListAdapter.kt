package com.ariel.readme.view.chats

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.repo.ChatRepository
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.ariel.readme.R
import com.ariel.readme.data.repo.ContactRepository
import com.ariel.readme.databinding.ChatCardViewFragmentBinding
import com.ariel.readme.factories.RepositoryFactory
import com.ariel.readme.factories.StorageFactory
import com.ariel.readme.factories.StoragePathFactory
import com.ariel.readme.view.chat.ChatActivity
import com.ariel.readme.services.AuthService
import com.squareup.picasso.Picasso


class ChatListAdapter(
    private val chats: List<Chat>,
    private val _context: Context
) :
    RecyclerView.Adapter<ChatListAdapter.ChatHolder>() {
    //This function prepares all things and gives them to ChatHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        return ChatHolder(
            ChatCardViewFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            _context
        )
    }

    //Everything is on theList
    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        //holder.imageView.setImageResource(ItemsViewModel.image)
        // sets the text to the textview from our itemHolder class
        holder.onDataBind(chats[position])
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return chats.size
    }

    inner class ChatHolder(
        private val _binding: ChatCardViewFragmentBinding,
        private val _context: Context
    ) :
        RecyclerView.ViewHolder(_binding.root) {
        //for card view
        var currentChat: Chat? = null
        fun onDataBind(chat: Chat) {
            currentChat = chat
            val me = AuthService.getCurrentFirebaseUser()
            val targetContact = chat.participants.filter { item -> item != me?.uid!! }
            if (targetContact.size > 0) {
                RepositoryFactory.getUserRepository().getUserById(targetContact[0])
                    .addOnSuccessListener { user ->
                        val displayName =
                            ContactRepository().getContactName(user.obj?.phone!!, _context)
                        _binding.UserName2.text =
                            if (displayName !== null) displayName else user.obj.phone

                        StorageFactory.getStorage()
                            .getDownloadUrl(StoragePathFactory.getProfilePicPath(user.obj))
                            .addOnSuccessListener { url ->
                                Picasso.get().load(url).into(_binding.UserProfile)
                            }
                    }
            } else {
                _binding.UserName2.text = _context.getString(R.string.unknown_user)
            }
        }

        init {
            _binding.deleteButton.setOnClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                //if the user click in the garb ask them if he is sure
                builder.setTitle("Are you sure?")
                builder.setMessage("Are you sure you want to remove this chat?")
                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    ChatRepository().removeChat(currentChat?.cid!!)
                }
                builder.setNegativeButton("No") { _: DialogInterface, _: Int ->

                }
                builder.show()
            }
            _binding.UserName2.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(_context, ChatActivity::class.java)
                    val bund = Bundle()
                    bund.putString(
                        ChatActivity.ARG_CHAT_ID, currentChat!!.cid
                    )
                    bund.putString(
                        ChatActivity.ARG_DISPLAY_NAME, _binding.UserName2.text.toString()
                    )
                   intent.putExtras(bund)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(_context, intent,bund )
                }
            })


        }

    }
}

