package com.wireless.memarize.viewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wireless.memarize.dataModel.Item
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.Incorrect

class IncorrectWordsRecyclerViewAdapter (private val itemList: ArrayList<Incorrect>) :

    RecyclerView.Adapter<IncorrectWordsRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val incorrect : Incorrect = itemList[position]
        holder.title.text = incorrect.word
        holder.desc.text = incorrect.desc
    }

    class ViewHolder(word: View) : RecyclerView.ViewHolder(word){
        var title:TextView = word.findViewById(R.id.word_title)
        var desc:TextView = word.findViewById(R.id.word_description)
    }

}
