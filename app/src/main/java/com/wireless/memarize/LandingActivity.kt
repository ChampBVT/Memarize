package com.wireless.memarize

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
    }

    override fun onStart() {
        super.onStart()
        object : CountDownTimer(3000, 1000) {
            override fun onFinish() {
                goToMainIntent()
            }
            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
    }

    private fun goToMainIntent() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}