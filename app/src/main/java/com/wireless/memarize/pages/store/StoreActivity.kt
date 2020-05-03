package com.wireless.memarize.pages.store

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wireless.memarize.R
import com.wireless.memarize.utils.*
import com.wireless.memarize.dataModel.Item
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
        val petName = getEncryptedSharePreferencesString("petName", this)
        getCurrentCoin()
        items.add(
            Item(
                "Recover your pet from tiredness",
                "10 secs",
                "120",
                "tired",
                R.drawable.bed,
                "bought a bed for $petName"
            )
        )
        items.add(
            Item(
                "Recover your pet from sickness",
                "20 secs",
                "150",
                "sick",
                R.drawable.vaccine,
                "vaccinated $petName"

            )
        )
        items.add(
            Item(
                "Recover your pet from boredom",
                "30 secs",
                "100",
                "bored",
                R.drawable.toy,
                "bought a toy for $petName"
            )
        )
        items.add(
            Item(
                "Recover your pet from thirstiness",
                "15 secs",
                "50",
                "thirsty",
                R.drawable.water,
                "bought a water for $petName"
            )
        )
        items.add(
            Item(
                "Recover your pet from injury",
                "40 secs",
                "300",
                "injured",
                R.drawable.injury,
                "treated $petName's injury"
            )
        )
        items.add(
            Item(
                "Recover your pet from hungriness",
                "20 secs",
                "50",
                "hungry",
                R.drawable.food,
                "bought a food for $petName"
            )
        )
        items.add(
            Item(
                "Recover your pet from dirtiness",
                "60 secs",
                "500",
                "dirty",
                R.drawable.bath,
                "bathed $petName"
            )
        )

        val adapter = ItemRecyclerViewAdapter(this, items) { getCurrentCoin() }
        itemRecyclerView.adapter = adapter
    }

    private fun getCurrentCoin(){
        currentCoin.text = getEncryptedSharePreferencesLong("coins", this).toString()
    }
}

