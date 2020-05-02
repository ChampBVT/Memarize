package com.wireless.memarize.pages.store

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.Chapter
import com.wireless.memarize.dataModel.Item
import com.wireless.memarize.utils.getEncryptedSharePreferencesLong
import com.wireless.memarize.viewAdapter.ItemRecyclerViewAdapter

class StoreActivity : AppCompatActivity() {

    lateinit var currentCoin : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val itemRecyclerView: RecyclerView = findViewById(R.id.itemRecyclerView)
        itemRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val items = ArrayList<Item>()
        currentCoin =  findViewById(R.id.CurrentCoinStore)
        getCurrentCoin()
        items.add(
            Item(
                "Recover your pet from tiredness",
                "10 secs",
                "120",
                "tired",
                R.drawable.bed
            )
        )
        items.add(
            Item(
                "Recover your pet from sickness",
                "20 secs",
                "150",
                "sick",
                R.drawable.vaccine
            )
        )
        items.add(
            Item(
                "Recover your pet from boredom",
                "30 secs",
                "100",
                "bored",
                R.drawable.toy
            )
        )
        items.add(
            Item(
                "Recover your pet from thirstiness",
                "15 secs",
                "50",
                "thirsty",
                R.drawable.water
            )
        )
        items.add(
            Item(
                "Recover your pet from injury",
                "40 secs",
                "300",
                "injured",
                R.drawable.injury
            )
        )
        items.add(
            Item(
                "Recover your pet from hungriness",
                "20 secs",
                "50",
                "hungry",
                R.drawable.food
            )
        )
        items.add(
            Item(
                "Recover your pet from dirtiness",
                "60 secs",
                "500",
                "dirty",
                R.drawable.bath
            )
        )

        val adapter = ItemRecyclerViewAdapter(this, items) { getCurrentCoin() }
        itemRecyclerView.adapter = adapter
    }

    private fun getCurrentCoin(){
        currentCoin.text = getEncryptedSharePreferencesLong("coins", this).toString()
    }
}

