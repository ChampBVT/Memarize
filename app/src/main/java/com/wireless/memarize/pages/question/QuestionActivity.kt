package com.wireless.memarize.pages.question;

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.wireless.memarize.R
import com.wireless.memarize.pages.main.MainActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.coroutines.*
import kotlin.collections.HashMap


class QuestionActivity : AppCompatActivity() {

    private lateinit var remainingTime: ProgressBar
    private var questionTime: Long = 10 *1000 //SECONDS * 1000
    private lateinit var database: FirebaseDatabase
    private lateinit var chapterTitle: TextView
    private lateinit var scoreText: TextView
    private lateinit var vocab: TextView
    private lateinit var words: HashMap<*, *>
    private lateinit var wordsPool: HashMap<*, *>
    private var choices: ArrayList<Button> = arrayListOf()
    private var score : Int = 0

    private lateinit var job : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        database = FirebaseDatabase.getInstance()
        remainingTime = findViewById(R.id.remainTime)
        vocab = findViewById(R.id.Vocabulary)
        scoreText = findViewById(R.id.Score)
        scoreText.text = "Score: $score"
        choices = arrayListOf(
            findViewById(R.id.choice1),
            findViewById(R.id.choice2),
            findViewById(R.id.choice3),
            findViewById(R.id.choice4)
        )
        chapterTitle = findViewById(R.id.Chapter)
        words = intent.getSerializableExtra("words") as HashMap<*, *>
        val title = intent.getStringExtra("chapterTitle")
        Log.e("get title", title)
        Log.e("get words", "$words")
        wordsPool = words.clone() as HashMap<*, *>
        chapterTitle.text = title
    }

    override fun onStart() {
        super.onStart()
        startNewQuestion()
    }

    private fun startNewQuestion(){
        job = GlobalScope.launch { // launch new coroutine in the scope of runBlocking
            while(words.isNotEmpty()){
                getNewQuestion()
                runOnUiThread{setProgressAnimate(remainingTime)}
                delay(questionTime)
            }
        }
        if(words.isEmpty())
            Toast.makeText(
                this, "No more question", Toast.LENGTH_SHORT
            ).show();
    }

    private fun getNewQuestion (){
        val wordsChoicePool = wordsPool.clone() as HashMap<*, *>
        val choicesIdxPool = arrayListOf(0,1,2,3)
        val i = Random().nextInt(words.size)
        val wordKeySet = ArrayList<Any>()
        wordKeySet.addAll(words.keys)
        Log.e("get correct words key", wordKeySet[i] as String)
        Log.e("get correct words value", words[wordKeySet[i]] as String)
        val j = Random().nextInt(4)
        this.runOnUiThread {
            vocab.text = wordKeySet[i] as CharSequence?
        }
        choices[j].text = words[wordKeySet[i]] as CharSequence?
        choices[j].setOnClickListener {
            job.cancel()
            this.runOnUiThread {
                remainingTime = findViewById(R.id.remainTime)
                score+= remainingTime.progress
                scoreText.text = "Score: $score"
            }
            startNewQuestion()
        }
        choicesIdxPool.remove(j)
        wordsChoicePool.remove(wordKeySet[i])
        for(index in choicesIdxPool){
            val k = Random().nextInt(wordsChoicePool.size)
            val wordsChoicePoolKeySet = ArrayList<Any>()
            wordsChoicePoolKeySet.addAll(wordsChoicePool.keys)
            choices[index].text = wordsChoicePool[wordsChoicePoolKeySet[k]] as CharSequence?
            Log.e("get wrong words key", wordsChoicePoolKeySet[k] as String)
            Log.e("get wrong words value", wordsChoicePool.toString())
            choices[index].setOnClickListener {
                job.cancel()
                startNewQuestion()
            }
            wordsChoicePool.remove(wordsChoicePoolKeySet[k])
        }
        words.remove(wordKeySet[i])
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //countDownTimer.cancel()
        job.cancel()
        finish()
    }

    private fun setProgressAnimate(pb: ProgressBar) {
        val animation =
            ObjectAnimator.ofInt(pb, "progress", 1000, 0)
        animation.duration = questionTime
        animation.interpolator = LinearInterpolator()
        animation.start()
    }

    private fun showToast() {
        Toast.makeText(
            this,
            "หมดเวลาแล้วจ้า",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun goToMainIntent() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
        finish()
    }

}


