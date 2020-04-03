package com.wireless.memarize

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signOutBtn: Button
    private lateinit var textHeader: TextView
    private lateinit var petInfo: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        textHeader = findViewById(R.id.welcomeHeader)
        petInfo = findViewById(R.id.petInfo)
        signOutBtn = findViewById(R.id.signOut)
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
    }

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
        val greeting = "Hi $userName!"
        val petSaid = "Your pet $petName is sleepy."
        textHeader.text = greeting
        petInfo.text = petSaid
    }

    private fun showToast(userName: String, email: String, uid: String) {
        Toast.makeText(
            this,
            "Current status \nName: $userName\nEmail: $email\nuid: $uid",
            Toast.LENGTH_LONG
        ).show()
    }

}
