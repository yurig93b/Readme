package com.ariel.readme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.repo.ChatRepository
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner


class Adapter( private val ListChats:  List<Chat>) :
    RecyclerView.Adapter<Adapter.ChatHolder>() {
    //This function prepares all things and gives them to ChatHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val view :View= LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_card_view, parent, false)
        return ChatHolder(view)
    }

    //Everything is on theList
    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        //holder.imageView.setImageResource(ItemsViewModel.image)
        // sets the text to the textview from our itemHolder class
        holder.onDataBind(ListChats[position])
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return ListChats.size
    }

    inner class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {//for card view

        val TheUserName: TextView
        val profileImage: ImageView
        val ButDeleteChat: ImageView
         var chat1:Chat?=null

        fun onDataBind(chat:Chat) {
            chat1=chat
            TheUserName.text=chat.cid
            }

        init {
            TheUserName = itemView.findViewById(R.id.UserName2)
            profileImage = itemView.findViewById(R.id.UserProfile)
            ButDeleteChat = itemView.findViewById(R.id.delete)
            ButDeleteChat.setOnClickListener {
                val builder=AlertDialog.Builder(itemView.context)
                val position: Int = adapterPosition
                //if the user click in the garb ask them if he is sure
                builder.setTitle("Are you sure!")
                builder.setMessage("are you sure you want to remove the chat?")
                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    ChatRepository().removeChat(chat1?.cid!!)
                }
                builder.setNegativeButton("No") { _: DialogInterface, _: Int ->
                    builder.show()
                }

            }
        }

    }
}