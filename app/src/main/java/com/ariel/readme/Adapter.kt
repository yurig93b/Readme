package com.ariel.readme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.repo.ChatRepository
import com.ariel.readme.services.AuthService
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


class Adapter(private val theList:  List<Chat>) :
    RecyclerView.Adapter<Adapter.ChatHolder>() {
    //הפונקציה הזו מכינה את כל הדברים ויתן אותם ChatHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_card_view, parent, false)
        return ChatHolder(view)
    }

    //עכשיו הכול נמצא בtheList
    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        //holder.imageView.setImageResource(ItemsViewModel.image)
        // sets the text to the textview from our itemHolder class
        holder.onDataBind(theList[position])
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return theList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.UserProfile)
        val textView: TextView = itemView.findViewById(R.id.UserName2)
    }


    inner class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val TheUserName: TextView
        val profileImage: ImageView
        val ButDeleteChat: ImageView
         var chat1:Chat?=null

        fun onDataBind(chat:Chat) {
            chat1=chat
            TheUserName.text=chat.cid
            }

        //Remains the part of clicking chat
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