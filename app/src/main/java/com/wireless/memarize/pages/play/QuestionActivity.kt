package com.wireless.memarize.pages.play;

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.wireless.memarize.R
import com.wireless.memarize.utils.displayChangeLanguage
import com.wireless.memarize.utils.displayChangeLanguageAlert
import com.wireless.memarize.utils.loadLocate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class QuestionActivity : AppCompatActivity() {

    private lateinit var remainingTime: ProgressBar
    private var questionTime: Long = 10 * 1000 //SECONDS * 1000
    private lateinit var database: FirebaseDatabase
    private lateinit var chapterTitle: TextView
    private lateinit var scoreText: TextView
    private lateinit var vocab: TextView
    private lateinit var words: HashMap<*, *>
    private lateinit var wordsRef: HashMap<*, *>
    private lateinit var wordsPool: HashMap<*, *>
    private lateinit var title: String
    private var wrongs =  mutableMapOf<String, String>()
    private var choices: ArrayList<Button> = arrayListOf()
    private var score : Int = 0
    private lateinit var job : Job
    private lateinit var changeLanguageBtn : Button
    private lateinit var backButton: Button
    private var notAnswer : Boolean = false
    private var j : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate(this)
        setContentView(R.layout.activity_question)
        database = FirebaseDatabase.getInstance()
        remainingTime = findViewById(R.id.remainTime)
        vocab = findViewById(R.id.Vocabulary)
        scoreText = findViewById(R.id.Score)
        backButton = findViewById(R.id.back)
        choices = arrayListOf(
            findViewById(R.id.choice1),
            findViewById(R.id.choice2),
            findViewById(R.id.choice3),
            findViewById(R.id.choice4)
        )
        chapterTitle = findViewById(R.id.Chapter)
        changeLanguageBtn = findViewById(R.id.changeLanguage)

        wordsRef = intent.getSerializableExtra("words") as HashMap<*, *>
        words = wordsRef.clone() as HashMap<*, *>
        title = intent.getStringExtra("chapterTitle") as String
        wordsPool = words.clone() as HashMap<*, *>
        scoreText.text = "${getString(R.string.score)}$score"
        chapterTitle.text = title

        changeLanguageBtn.setOnClickListener {
            displayChangeLanguageAlert(this, this)
        }

        backButton.setOnClickListener{
            onBackPressed();
        }

    }

    override fun onStart() {
        super.onStart()
        startNewQuestion(true)
    }

    private fun startNewQuestion(firstQuestion : Boolean){
        job = GlobalScope.launch {
            while(words.isNotEmpty()){
                if(!firstQuestion || notAnswer)
                    delay(1000)
                notAnswer = true
                getNewQuestion()
                runOnUiThread{setProgressAnimate(remainingTime)}
                delay(questionTime)
                if(notAnswer){
                    runOnUiThread{
                        choices[j].setBackgroundResource(R.drawable.true_choice_button)
                        choices[j].setTextColor(resources.getColorStateList(R.color.black))
                    }
                }
            }
            goToSumScore()
        }
    }

    private fun getNewQuestion (){
        val words1 = words.clone() as HashMap<*, *>
        val wordsChoicePool = wordsPool.clone() as HashMap<*, *>
        val choicesIdxPool = arrayListOf(0,1,2,3)
        val i = Random().nextInt(words1.size)
        val wordKeySet = ArrayList<Any>()
        wordKeySet.addAll(words1.keys)
        j = Random().nextInt(4)
        val text = words1[wordKeySet[i]] as CharSequence
        this.runOnUiThread {
            choices[j].setBackgroundResource(R.drawable.choice_button)
            choices[j].setTextColor(resources.getColorStateList(R.color.white))
            vocab.text = text
            choices[j].text = wordKeySet[i] as CharSequence?
        }
        choices[j].setOnClickListener {
            this.runOnUiThread {
                choices[j].setBackgroundResource(R.drawable.true_choice_button)
                choices[j].setTextColor(resources.getColorStateList(R.color.black))
                remainingTime = findViewById(R.id.remainTime)
                score+= remainingTime.progress
                scoreText.text = "${getString(R.string.score)}$score"
            }
            for (idx in choicesIdxPool)
                choices[idx].setOnClickListener(null)
            choices[j].setOnClickListener(null)
            notAnswer = false
            job.cancel()
            startNewQuestion(false)
        }
        choicesIdxPool.remove(j)
        wordsChoicePool.remove(wordKeySet[i])
        for(index in choicesIdxPool){
            val wordsChoicePoolKeySet = ArrayList<Any>(wordsChoicePool.keys)
            val k = Random().nextInt(wordsChoicePool.size)
            val textChoice = wordsChoicePoolKeySet[k] as CharSequence?
            this.runOnUiThread {
                choices[index].setBackgroundResource(R.drawable.choice_button)
                choices[index].setTextColor(resources.getColorStateList(R.color.white))
                choices[index].text = textChoice
            }
            choices[index].setOnClickListener {
                choices[index].setBackgroundResource(R.drawable.false_choice_button)
                wrongs[wordKeySet[i] as String] = words1[wordKeySet[i]] as String
                for (idx in choicesIdxPool)
                    choices[idx].setOnClickListener(null)
                choices[j].setBackgroundResource(R.drawable.true_choice_button)
                choices[j].setTextColor(resources.getColorStateList(R.color.black))
                choices[j].setOnClickListener(null)
                notAnswer = false
                job.cancel()
                startNewQuestion(false)
            }
            wordsChoicePool.remove(wordsChoicePoolKeySet[k])
        }
        words.remove(wordKeySet[i])
    }

    override fun onBackPressed() {
        super.onBackPressed()
        job.cancel()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun setProgressAnimate(pb: ProgressBar) {
        val animation =
            ObjectAnimator.ofInt(pb, "progress", 1000, 0)
        animation.duration = questionTime
        animation.interpolator = LinearInterpolator()
        animation.start()
    }

    private fun goToSumScore() {
        val intent = Intent(this@QuestionActivity, SumScoreActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        intent.putExtra("wrongs", wrongs as HashMap<String, String>)
        intent.putExtra("totalWords", wordsPool.size)
        intent.putExtra("scores", score)
        intent.putExtra("chapterTitle", title)
        startActivity(intent)
        finish()
    }

}


