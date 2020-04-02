package com.wireless.memarize

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recyclerViewAdapter (val itemList: ArrayList<Item>) :
    RecyclerView.Adapter<recyclerViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var itemm:Item = itemList[position]
        holder.title.text = itemm.title
        holder.desc1.text = itemm.desc1
        holder.desc2.text = itemm.desc2
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var title.TextView = item.findViewById(R.id.tvTitle)
        var desc1.TextView = item.findViewById(R.id.tvDescription1)
        var desc2.TextView = item.findViewById(R.id.tvDescription2)
    }
}