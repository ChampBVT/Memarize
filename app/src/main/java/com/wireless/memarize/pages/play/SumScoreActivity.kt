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
    private lateinit var correctedWord: TextView
    private lateinit var scoreText: TextView
    private lateinit var incorrectWordsHeader: TextView
    private lateinit var chapterText: TextView
    private var score: Int = 0
    private lateinit var changeLanguageBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate(this)
        setContentView(R.layout.activity_sum_score)
        val incorrectWordRecyclerView: RecyclerView = findViewById(R.id.incorrectWordRecyclerView)
        incorrectWordRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        correctedWord = findViewById(R.id.Correct)
        scoreText = findViewById(R.id.SumScore)
        changeLanguageBtn = findViewById(R.id.changeLanguage)
        newCoin = findViewById(R.id.NewCoin)
        continueBtn = findViewById(R.id.ContinueBtn)
        chapterText = findViewById(R.id.Chapter)
        incorrectWordsHeader = findViewById(R.id.textView8)

        wrongs = intent.getSerializableExtra("wrongs") as HashMap<*, *>
        val wrongsList = ArrayList<Incorrect>()
        for(key in wrongs.keys)
            wrongsList.add(Incorrect(key as String, wrongs[key] as String))

        score = intent.getIntExtra("scores", -1)
        val totalWords = intent.getIntExtra("totalWords", -1)

        if(wrongs.isNotEmpty()) {
            val adapter = IncorrectWordsRecyclerViewAdapter(wrongsList)
            incorrectWordRecyclerView.adapter = adapter
        } else if(score != 0){
            incorrectWordsHeader.text = getString(R.string.full)
        } else {
            incorrectWordsHeader.text = getString(R.string.noans)
        }

        val percentage = "%.0f".format(((totalWords.toDouble()-wrongs.size.toDouble())/totalWords.toDouble())*100)
        if(score != 0)
            correctedWord.text = "${getString(R.string.correct)}${percentage}% (${totalWords-wrongs.size}/${totalWords})"
        else
            correctedWord.text = "${getString(R.string.correct)}${0}% (0/${totalWords})"

        chapterText.text = intent.getStringExtra("chapterTitle")
        scoreText.text = "${getString(R.string.score)}$score"
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
        changeLanguageBtn.setOnClickListener {
            displayChangeLanguage(this, this)
        }
    }
}
