package com.ariel.readme.view.hotwords

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ariel.readme.R
import com.ariel.readme.data.repo.HotWordRepository
import com.ariel.readme.services.AuthService

class HotWordsRecyclerAdapter(private val list : List<String>) : RecyclerView.Adapter<HotWordsRecyclerAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_layout, parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.textView.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView : TextView
        val imageView : ImageView

        init{
            textView  = itemView.findViewById(R.id.recycler_text)
            imageView = itemView.findViewById(R.id.remove_word)

            imageView.setOnClickListener{
                val position : Int = adapterPosition
                val text : String = list[position]
                val clearAlert = AlertDialog.Builder(itemView.getContext())
                clearAlert.setTitle("Warning")
                clearAlert.setMessage("are you sure you want to remove $text?")
                clearAlert.setCancelable(false)
                clearAlert.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                    HotWordRepository().removeHotWord(text, AuthService.getCurrentFirebaseUser()!!.uid)
                }
                clearAlert.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
                clearAlert.show()
            }
        }
    }
}