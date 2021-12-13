package com.ariel.readme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ariel.readme.data.repo.HotWordRepository

class RecyclerAdapter(private val list : List<String>) : RecyclerView.Adapter<RecyclerAdapter.Holder>() {

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
                HotWordRepository().removeHotWord(text)
            }
        }
    }
}