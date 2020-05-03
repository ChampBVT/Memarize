package com.wireless.memarize.pages.store

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
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
import java.util.*
import kotlin.collections.ArrayList

class StoreActivity : AppCompatActivity() {

    private lateinit var changeLanguageBtn : Button
    private lateinit var currentCoin : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate() // Add (2)
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

        // Add (3) Change language
        changeLanguageBtn = findViewById(R.id.changeLanguage)

        changeLanguageBtn.setOnClickListener {
            displayChangeLanguage()
        }
        // ------ end (Add 3) -------
    }

    // Add (4) Change language
    private fun displayChangeLanguage() {
        val listLang = arrayOf("EN", "TH")

        val mBuilder = AlertDialog.Builder(this@StoreActivity)
        mBuilder.setTitle("@string/Select_Language")
        mBuilder.setSingleChoiceItems(listLang, -1)
        { dialog, which ->
            if (which == 0) {
                setLocate("en")
                recreate()
            } else {
                setLocate("th")
                recreate()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun setLocate(language: String?){
        val locale = Locale(language)

        Locale.setDefault(locale)
        val config = Configuration()
        config.locale= locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("myLanguage", language)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("myLanguage", "")
        setLocate(language)
    }

    private fun getCurrentCoin(){
        currentCoin.text = getEncryptedSharePreferencesLong("coins", this).toString()
    }
}
