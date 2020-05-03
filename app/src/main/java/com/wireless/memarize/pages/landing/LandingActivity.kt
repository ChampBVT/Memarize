package com.wireless.memarize.pages.landing

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.User
import com.wireless.memarize.pages.login.LoginActivity
import com.wireless.memarize.pages.main.MainActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var countDownTimer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        auth = FirebaseAuth.getInstance()
    }

    private fun getUserInfo(uid: String) {
        val database = FirebaseDatabase.getInstance().reference
        var userName: String?
        var email: String?
        var petName: String?
        var petType: String?
        var coins: Long
        database.child("users").child(uid).addListenerForSingleValueEvent(
            object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        userName = user.name
                        email = user.email
                        petName = user.petName
                        petType = user.petType
                        coins = user.coins
                        setEncryptedSharePreferences(userName!!, email!!, uid, petName!!, petType!!, coins)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }

            })
    }

    @SuppressLint("ApplySharedPref")
    private fun setEncryptedSharePreferences(userName: String, email: String, uid: String, petName: String, petType: String, coins: Long) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "PreferencesFilename",
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.edit()
            .putString("userName", userName)
            .putString("email", email)
            .putString("uid", uid)
            .putString("petName", petName)
            .putString("petType", petType)
            .putLong("coins", coins)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                val user = auth.currentUser;
                if (user == null) {
                    goToLoginIntent()
                } else {
                    getUserInfo(user.uid)
                    goToMainIntent()
                }
            }
            override fun onTick(millisUntilFinished: Long) {}
        }.start()
    }

    private fun goToMainIntent() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToLoginIntent() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        countDownTimer.cancel()
        finish()
    }


}