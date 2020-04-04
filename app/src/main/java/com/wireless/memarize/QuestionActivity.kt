package com.wireless.memarize;

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class QuestionActivity : AppCompatActivity() {

    private lateinit var remainingTime: ProgressBar
    private var questionTime: Long = 10 *1000 //SECONDS * 1000
    private lateinit var countDownTimer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        remainingTime = findViewById(R.id.remainTime)
        countDownTimer = object : CountDownTimer(questionTime, 1000) {
            override fun onFinish() {
                showToast()
                goToMainIntent()
            }

            override fun onTick(millisUntilFinished: Long) {}
        }.start()
        setProgressAnimate(remainingTime)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        countDownTimer.cancel()
        finish()
    }

    private fun setProgressAnimate(pb: ProgressBar) {
        val animation =
            ObjectAnimator.ofInt(pb, "progress", pb.progress, 0)
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
        startActivity(intent)
        finish()
    }
}
