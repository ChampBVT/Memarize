package com.wireless.memarize.pages.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.wireless.memarize.pages.play.ChapterActivity
import com.wireless.memarize.R
import com.wireless.memarize.pages.login.LoginActivity
import com.wireless.memarize.pages.store.StoreActivity
import com.wireless.memarize.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signOutBtn: Button
    private lateinit var textHeader: TextView
    private lateinit var petInfo: TextView
    private lateinit var imageView: ImageView
    private lateinit var storeBtn: Button
    private lateinit var playBtn: Button
    private lateinit var coins: TextView
    private lateinit var changeLanguageBtn: Button
    private var context: Context = this
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate(this)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        textHeader = findViewById(R.id.welcomeHeader)
        petInfo = findViewById(R.id.petInfo)
        signOutBtn = findViewById(R.id.signOut)
        storeBtn = findViewById(R.id.store)
        playBtn = findViewById(R.id.play)
        coins = findViewById(R.id.CurrentCoin)
        changeLanguageBtn = findViewById(R.id.changeLanguage)
        imageView = findViewById(R.id.petStatus)
        signOutBtn.setOnClickListener {
            auth.signOut()
            goToLoginIntent()
        }
        storeBtn.setOnClickListener {
            goToStoreIntent()
        }
        playBtn.setOnClickListener {
            goToChapterIntent()
        }
        changeLanguageBtn.setOnClickListener {
            displayChangeLanguage(this, this)
        }
    }

    override fun onStart() {
        super.onStart()
        val userName = getEncryptedSharePreferencesString("userName", this)
        "${getString(R.string.Hi)} $userName"
        textHeader.text = "${getString(R.string.Hi)} $userName"
        coins.text = getEncryptedSharePreferencesLong("coins", this).toString()
        setPetStatus()
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    private fun setPetStatus() {
        job = GlobalScope.launch {
            val petStatusPool = arrayListOf("tired", "sick", "bored", "thirsty", "injured", "hungry", "dirty")
            val petName = getEncryptedSharePreferencesString("petName", context)
            var currentTimestamp : Long
            var savedTimeStamp : Long
            var currentPetStatus : String
            var i : Int
            var petSaid : String
            while (true) {
                currentTimestamp = System.currentTimeMillis()
                savedTimeStamp = getEncryptedSharePreferencesLong("timestamp", context)
                currentPetStatus = getEncryptedSharePreferencesString("status", context)
                setPetImage(currentPetStatus)
                if (currentTimestamp > savedTimeStamp && !petStatusPool.contains(currentPetStatus)) {
                    i = Random().nextInt(petStatusPool.size)
                    setEncryptedSharePreferencesString("status", petStatusPool[i], context)
                }
                petSaid = "${getString(R.string.petStatus1)} $petName ${getString(R.string.petStatus2)}" +
                        mapStatusToStringResource(getEncryptedSharePreferencesString("status", context))
                petInfo.text = petSaid
                delay(1000)
            }
        }
    }

    private fun mapStatusToStringResource(status: String): String {
        var stringStatus: String = "not found"
        when (status) {
            "normal" -> stringStatus = getString(R.string.normal)
            "tired" -> stringStatus = getString(R.string.tired)
            "dirty" -> stringStatus = getString(R.string.dirty)
            "hungry" -> stringStatus = getString(R.string.hungry)
            "sick" -> stringStatus = getString(R.string.sick)
            "bored" -> stringStatus = getString(R.string.bored)
            "injured" -> stringStatus = getString(R.string.injured)
            "thirsty" -> stringStatus = getString(R.string.thirsty)
        }
        return stringStatus
    }

    private fun getResourceByName(status: String, type: String): Int {
        val petType = getEncryptedSharePreferencesString("petType", context).toLowerCase()
        return resources.getIdentifier("$petType$status", type, context.packageName)
    }

    private fun setPetImage(status: String) {
        runOnUiThread {
            imageView.setImageResource(getResourceByName(status, "drawable"))
        }
    }

    private fun goToLoginIntent() {
        val intent = Intent(this, LoginActivity::class.java)
        job.cancel()
        startActivity(intent)
        finish()
    }

    private fun goToStoreIntent() {
        val intent = Intent(this, StoreActivity::class.java)
        job.cancel()
        startActivity(intent)
    }

    private fun goToChapterIntent() {
        val intent = Intent(this, ChapterActivity::class.java)
        job.cancel()
        startActivity(intent)
    }
}
