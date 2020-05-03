package com.wireless.memarize.pages.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
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
        imageView.setImageResource(R.drawable.c1)
    }

    private fun goToLoginIntent() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToStoreIntent() {
        val intent = Intent(this, StoreActivity::class.java)
        startActivity(intent)
    }

    private fun goToChapterIntent() {
        val intent = Intent(this, ChapterActivity::class.java)
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
