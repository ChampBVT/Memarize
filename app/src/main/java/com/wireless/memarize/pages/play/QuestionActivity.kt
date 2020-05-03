package com.wireless.memarize.pages.play;

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.wireless.memarize.R
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
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
    private var wrongs: ArrayList<String> = arrayListOf()
    private var choices: ArrayList<Button> = arrayListOf()
    private var score : Int = 0
    private lateinit var job : Job
    private lateinit var changeLanguageBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate() // Add (2)
        setContentView(R.layout.activity_question)

        database = FirebaseDatabase.getInstance()
        remainingTime = findViewById(R.id.remainTime)
        vocab = findViewById(R.id.Vocabulary)
        scoreText = findViewById(R.id.Score)
        scoreText.text = "Score: $score"
        scoreText.text = getString(R.string.score).plus("$score")
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

        val mBuilder = AlertDialog.Builder(this@QuestionActivity)
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
        if(words.isEmpty()) {
            Toast.makeText(
                this, "No more question", Toast.LENGTH_SHORT
            ).show();
            goToSumScore()
        }
    }

    private fun getNewQuestion (){
        val a = Mutex()
        val words1 = words.clone() as HashMap<*, *>
        val wordsChoicePool = wordsPool.clone() as HashMap<*, *>
        val choicesIdxPool = arrayListOf(0,1,2,3)
        val i = Random().nextInt(words1.size)
        val wordKeySet = ArrayList<Any>()
        wordKeySet.addAll(words1.keys)
        val j = Random().nextInt(4)
        val text = words1[wordKeySet[i]] as CharSequence?
        this.runOnUiThread {
            vocab.text = wordKeySet[i] as CharSequence?
            choices[j].text = text
        }
        choices[j].setOnClickListener {
            //Thread.sleep(1500)
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
            val wordsChoicePoolKeySet = ArrayList<Any>(wordsChoicePool.keys)
            val k = Random().nextInt(wordsChoicePool.size)
            val textChoice = wordsChoicePool[wordsChoicePoolKeySet[k]] as CharSequence?
            this.runOnUiThread {
                choices[index].text = textChoice
            }
            choices[index].setOnClickListener {
                Log.e("wrong word", wordKeySet[i].toString())
                wrongs.add(wordKeySet[i] as String)
                //Thread.sleep(1500)
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

    private fun goToSumScore() {
        val intent = Intent(this, SumScoreActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        Log.e("get words", "$wrongs")
        intent.putStringArrayListExtra("wrongs", wrongs)
        intent.putExtra("scores", score)
        startActivity(intent)
        finish()
    }

}


