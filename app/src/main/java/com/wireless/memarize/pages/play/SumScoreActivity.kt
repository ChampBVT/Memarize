package com.wireless.memarize.pages.play

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.Incorrect
import com.wireless.memarize.dataModel.Item
import com.wireless.memarize.pages.main.MainActivity
import com.wireless.memarize.viewAdapter.IncorrectWordsRecyclerViewAdapter

class SumScoreActivity : AppCompatActivity() {

    private lateinit var continueBtn: Button
    private lateinit var wrongs: HashMap<*, *>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sum_score)

        val incorrectWordRecyclerView: RecyclerView = findViewById<RecyclerView>(R.id.incorrectWordRecyclerView)
        incorrectWordRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        continueBtn = findViewById(R.id.ContinueBtn)
        wrongs = intent.getSerializableExtra("wrongs") as HashMap<*, *>
        val wrongsList = ArrayList<Incorrect>()
        Log.e("get words", "$wrongs")
        for(key in wrongs.keys){
            wrongsList.add(Incorrect(key as String, wrongs[key] as String))
        }
        val adapter = IncorrectWordsRecyclerViewAdapter(wrongsList)
        incorrectWordRecyclerView.adapter = adapter
        continueBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            finish()
        }
    }
}
