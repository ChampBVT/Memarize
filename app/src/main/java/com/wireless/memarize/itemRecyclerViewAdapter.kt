package com.wireless.memarize

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class itemRecyclerViewAdapter (private val itemList: ArrayList<Item>) :

    RecyclerView.Adapter<itemRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items:Item = itemList[position]
        holder.title.text = items.itemTitle
        holder.desc1.text = items.itemDesc1
        holder.desc2.text = items.itemDesc1
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var title: TextView = item.findViewById(R.id.itemTitle)
        var desc1:TextView = item.findViewById(R.id.itemDesc1)
        var desc2:TextView = item.findViewById(R.id.itemDesc2)
    }
}