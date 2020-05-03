package com.wireless.memarize.pages.store

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.Item
import com.wireless.memarize.viewAdapter.ItemRecyclerViewAdapter
import java.util.*
import kotlin.collections.ArrayList

class StoreActivity : AppCompatActivity() {

    private lateinit var changeLanguageBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate() // Add (2)
        setContentView(R.layout.activity_store)

        val itemRecyclerView: RecyclerView = findViewById(R.id.itemRecyclerView)

        itemRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val item = ArrayList<Item>()

        item.add(
            Item(
                "@string/Recover_your_pet_from_tiredness",
                "@string/time_30mins",
                "x120"
            )
        )
        item.add(
            Item(
                "@string/Recover_your_pet_from_sickness",
                "@string/time_20mins",
                "x150"
            )
        )
        item.add(
            Item(
                "@string/Recover_your_pet_from_boredom",
                "@string/time_300mins",
                "x20"
            )
        )
        item.add(
            Item(
                "@string/Recover_your_pet_from_thirstiness",
                "@string/time_3mins",
                "x12000"
            )
        )
        item.add(
            Item(
                "@string/Recover_your_pet_from_injury",
                "@string/time_1mins",
                "x12"
            )
        )
        item.add(
            Item(
                "@string/Recover_your_pet_from_injury",
                "@string/time_1mins",
                "x1"
            )
        )
        item.add(
            Item(
                "@string/Recover_your_pet_from_injury",
                "@string/time_1mins",
                "x2"
            )
        )

        val adapter = ItemRecyclerViewAdapter(item)
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
    //------ end (Add 4) -------
}