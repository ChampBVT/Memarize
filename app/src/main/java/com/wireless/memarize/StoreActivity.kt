package com.wireless.memarize

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val itemRecyclerView: RecyclerView = findViewById(R.id.itemRecyclerView)

        itemRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val item = ArrayList<Item>()

        item.add(Item("Recover your pet from tiredness", "30 mins", "x120"))
        item.add(Item("Recover your pet from sickness", "20 mins", "x150"))
        item.add(Item("Recover your pet from boredom", "300 mins", "x20"))
        item.add(Item("Recover your pet from thirstiness", "3 mins", "x12000"))
        item.add(Item("Recover your pet from injury", "1 mins", "x120000000"))

        val adapter = itemRecyclerViewAdapter(item)
        itemRecyclerView.adapter = adapter
    }
}