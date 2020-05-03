package com.wireless.memarize.pages.play

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.Incorrect
import com.wireless.memarize.dataModel.Item
import com.wireless.memarize.pages.main.MainActivity
import com.wireless.memarize.utils.*
import com.wireless.memarize.viewAdapter.IncorrectWordsRecyclerViewAdapter
import java.util.*
import kotlin.collections.ArrayList

class SumScoreActivity : AppCompatActivity() {

    private lateinit var continueBtn: Button
    private lateinit var wrongs: HashMap<*, *>
    private lateinit var newCoin: TextView
    private lateinit var incorrectWordsHeader: TextView
    private var score: Int = 0
    private lateinit var changeLanguageBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate() // Add (2)
        setContentView(R.layout.activity_sum_score)

        val incorrectWordRecyclerView: RecyclerView = findViewById(R.id.incorrectWordRecyclerView)
        incorrectWordRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        newCoin = findViewById(R.id.NewCoin)
        continueBtn = findViewById(R.id.ContinueBtn)
        wrongs = intent.getSerializableExtra("wrongs") as HashMap<*, *>
        val wrongsList = ArrayList<Incorrect>()
        Log.e("get words", "$wrongs")
        for(key in wrongs.keys){
            wrongsList.add(Incorrect(key as String, wrongs[key] as String))
        }
        incorrectWordsHeader = findViewById(R.id.textView8)
        score = intent.getIntExtra("scores", -1)
        Log.e("get words", "$wrongs")
        Log.e("score :", "$score")
        if(wrongs.isNotEmpty()) {
            val adapter = IncorrectWordsRecyclerViewAdapter(wrongsList)
            incorrectWordRecyclerView.adapter = adapter
        } else {
            incorrectWordsHeader.text = "Congratulations! \nYou get every word corrected."
        }
        score /= 10
        newCoin.text = " +$score"
        val newCoins = getEncryptedSharePreferencesLong("coins", this) + score
        setEncryptedSharePreferencesLong("coins", newCoins, this)
        setRealtimeDatabaseValue("coins", newCoins, this)
        continueBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            finish()
        }

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

        val mBuilder = AlertDialog.Builder(this@SumScoreActivity)
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
