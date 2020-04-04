package com.wireless.memarize

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChapterRecyclerViewAdapter (private val chapterList: ArrayList<Chapter>) :

    RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapters: Chapter = chapterList[position]
        holder.title.text = chapters.chapterTitle
        holder.wordsL.text = chapters.wordCount.toString()
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var title:TextView = item.findViewById(R.id.chapterTitle)
        var wordsL:TextView = item.findViewById(R.id.wordsLearnt)
    }
}