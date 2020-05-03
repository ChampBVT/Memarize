package com.wireless.memarize.viewAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.Chapter
import com.wireless.memarize.dataModel.Item
import com.wireless.memarize.utils.*


class ItemRecyclerViewAdapter(
    val context: Context,
    private val itemList: ArrayList<Item>,
    val setCurrentCoin: () -> Unit
) :

    RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items: Item = itemList[position]
        holder.title.text = items.itemTitle
        holder.desc1.text = items.itemDesc1
        holder.desc2.text = items.itemDesc2
        holder.img.setImageResource(items.src)
        holder.itemView.setOnClickListener {
            var currentCoins = getEncryptedSharePreferencesLong("coins", context)
            if(currentCoins>=items.itemDesc2.toLong()){
                currentCoins -= items.itemDesc2.toLong()
                setEncryptedSharePreferencesLong("coins", currentCoins, context)
                if(getEncryptedSharePreferencesString("status", context) == items.tag) {
                    setEncryptedSharePreferencesString("status", "normal", context)
                    setEncryptedSharePreferencesLong(
                        "timestamp",
                        System.currentTimeMillis() + (items.itemDesc1.split(" ")[0].toLong() * 1000),
                        context
                    )
                }
                setEncryptedSharePreferencesLong("", currentCoins, context)
                setRealtimeDatabaseValue("coins", currentCoins, context)
                setCurrentCoin()
                Toast.makeText(
                    context,
                    "Successfully ${items.action}",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    context,
                    "Not enough coins",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var title:TextView = item.findViewById(R.id.itemTitle)
        var desc1:TextView = item.findViewById(R.id.itemDesc1)
        var desc2:TextView = item.findViewById(R.id.itemDesc2)
        var img: ImageView = item.findViewById(R.id.itemImg)
    }
}
