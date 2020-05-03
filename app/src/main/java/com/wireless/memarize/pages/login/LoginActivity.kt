package com.wireless.memarize.pages.login

import android.annotation.SuppressLint
import com.wireless.memarize.utils.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wireless.memarize.R
import com.wireless.memarize.pages.register.RegisterActivity
import com.wireless.memarize.dataModel.User
import com.wireless.memarize.pages.main.MainActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var registerBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button
    private lateinit var changeLanguageBtn : Button

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(arg0: Context, intent: Intent) {
            val action = intent.action
            if (action == "Close_Login_Activity") {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate(this)
        setContentView(R.layout.activity_login)

        emailEt = findViewById(R.id.email_et)
        passwordEt = findViewById(R.id.password_et)
        loginBtn = findViewById(R.id.login_bttn)
        registerBtn = findViewById(R.id.register)

        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener{
            loginBtn.isEnabled = false
            val email: String = emailEt.text.toString()
            val password: String = passwordEt.text.toString()
            if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                Toast.makeText(this@LoginActivity, getString(R.string.fill), Toast.LENGTH_SHORT).show()
                loginBtn.isEnabled = true
            }else{
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful){
                        val user = auth.currentUser;
                        if (user != null) {
                            getUserInfo(user.uid)
                            Toast.makeText(this, getString(R.string.successful), Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show()
                        loginBtn.isEnabled = true
                    }
                })
            }
        }

        registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        registerReceiver(broadcastReceiver, IntentFilter("Close_Login_Activity"))

        changeLanguageBtn = findViewById(R.id.changeLanguage)

        changeLanguageBtn.setOnClickListener {
            displayChangeLanguage(this, this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
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
                        setEncryptedSharePreferences(userName!!, email!!, uid, petName!!, petType!!, coins, this@LoginActivity)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }

            })
    }
}
