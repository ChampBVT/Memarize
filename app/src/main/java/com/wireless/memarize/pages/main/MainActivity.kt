package com.wireless.memarize.pages.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
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
    private lateinit var changeLanguageBtn : Button
    private var context : Context = this
    private lateinit var job : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate() // Add (2)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        textHeader = findViewById(R.id.welcomeHeader)
        petInfo = findViewById(R.id.petInfo)
        signOutBtn = findViewById(R.id.signOut)
        storeBtn = findViewById(R.id.store)
        playBtn = findViewById(R.id.play)
        coins = findViewById(R.id.CurrentCoin)
//        val sharedPref =
//            this.getSharedPreferences(getString(R.string.name_key), Context.MODE_PRIVATE)
//        Toast.makeText(
//            this,
//            "Current status \nName: ${sharedPref.getString("name", "kuy")}",
//            Toast.LENGTH_LONG
//        ).show()
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

        val mBuilder = AlertDialog.Builder(this@MainActivity)
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
        getEncryptedSharePreferences()
        setPetStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun setPetStatus(){
        job = GlobalScope.launch {
                // launch new coroutine in the scope of runBlocking
                val petStatusPool = arrayListOf("tired", "sick", "bored", "thirsty", "injured", "hungry", "dirty")
                while(true){
                    val currentTimestamp = System.currentTimeMillis()
                    val savedTimeStamp = getEncryptedSharePreferencesLong("timestamp", context)
                    val currentPetStatus = getEncryptedSharePreferencesString("status", context)
                    Log.e("get status", getEncryptedSharePreferencesString("status", context))
                    setPetImage(currentPetStatus)
                    if(currentTimestamp > savedTimeStamp && !petStatusPool.contains(currentPetStatus)){
                        val i = Random().nextInt(petStatusPool.size)
                        setEncryptedSharePreferencesString("status", petStatusPool[i], context)
                        Log.e("set status", petStatusPool[i])
                    } else if(currentTimestamp < savedTimeStamp && petStatusPool.contains(currentPetStatus)){
                        Log.e("set to normal", "normal")
                    }
                    delay(1000)
                }
            }
    }

    private fun getResourceByName(status: String, type: String): Int {
        val petType = getEncryptedSharePreferencesString("petType", context).toLowerCase()
        return resources.getIdentifier("$petType$status" , type, context.packageName)
    }

    private fun setPetImage(status : String){
        runOnUiThread{
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

    private fun getEncryptedSharePreferences() {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences = EncryptedSharedPreferences.create(
            "PreferencesFilename",
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val userName = sharedPreferences.getString("userName", "default")
        val petName = sharedPreferences.getString("petName", "default")
        val coin = sharedPreferences.getLong("coins", -1)
        val greeting = "Hi $userName!"
        val petSaid = "Your pet $petName is sleepy."
        textHeader.text = greeting
        petInfo.text = petSaid
        coins.text = " $coin"
    }

    private fun showToast(userName: String, email: String, uid: String) {
        Toast.makeText(
            this,
            "Current status \nName: $userName\nEmail: $email\nuid: $uid",
            Toast.LENGTH_LONG
        ).show()
    }

}
