package com.wireless.memarize.pages.store

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wireless.memarize.R
import com.wireless.memarize.utils.*
import com.wireless.memarize.dataModel.Item
import com.wireless.memarize.viewAdapter.ItemRecyclerViewAdapter
import kotlin.collections.ArrayList

class StoreActivity : AppCompatActivity() {

    private lateinit var changeLanguageBtn : Button
    private lateinit var currentCoin : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate(this)
        setContentView(R.layout.activity_store)
        changeLanguageBtn = findViewById(R.id.changeLanguage)
        val itemRecyclerView: RecyclerView = findViewById(R.id.itemRecyclerView)
        itemRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val items = ArrayList<Item>()
        currentCoin =  findViewById(R.id.CurrentCoinStore)
        val petName = getEncryptedSharePreferencesString("petName", this)
        getCurrentCoin()
        items.add(
            Item(
                getString(R.string.Recover_your_pet_from_tiredness),
                "10 secs",
                "120",
                "tired",
                R.drawable.bed,
                "${getString(R.string.bed)} $petName"
            )
        )
        items.add(
            Item(
                getString(R.string.Recover_your_pet_from_sickness),
                "20 secs",
                "150",
                "sick",
                R.drawable.vaccine,
                "${getString(R.string.vaccine)} $petName"
            )
        )
        items.add(
            Item(
                getString(R.string.Recover_your_pet_from_boredom),
                "30 secs",
                "100",
                "bored",
                R.drawable.toy,
                "${getString(R.string.toy)} $petName"
            )
        )
        items.add(
            Item(
                getString(R.string.Recover_your_pet_from_thirstiness),
                "15 secs",
                "50",
                "thirsty",
                R.drawable.water,
                "${getString(R.string.water)} $petName"
            )
        )
        items.add(
            Item(
                getString(R.string.Recover_your_pet_from_injury),
                "40 secs",
                "300",
                "injured",
                R.drawable.injury,
                "${getString(R.string.medkit)} $petName"
            )
        )
        items.add(
            Item(
                getString(R.string.Recover_your_pet_from_hungriness),
                "20 secs",
                "50",
                "hungry",
                R.drawable.food,
                "${getString(R.string.food)} $petName"
            )
        )
        items.add(
            Item(
                getString(R.string.Recover_your_pet_from_dirtiness),
                "60 secs",
                "500",
                "dirty",
                R.drawable.bath,
                "${getString(R.string.bathe)} $petName"
            )
        )
        val adapter = ItemRecyclerViewAdapter(this, items) { getCurrentCoin() }
        itemRecyclerView.adapter = adapter

        changeLanguageBtn.setOnClickListener {
            displayChangeLanguage(this, this)
        }
    }

    private fun getCurrentCoin(){
        currentCoin.text = getEncryptedSharePreferencesLong("coins", this).toString()
    }
}
