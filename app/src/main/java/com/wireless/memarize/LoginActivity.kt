package com.wireless.memarize

import android.annotation.SuppressLint
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
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity() {
    private lateinit var registerBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button

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
        setContentView(R.layout.activity_login)

        emailEt = findViewById(R.id.email_et)
        passwordEt = findViewById(R.id.password_et)
        loginBtn = findViewById(R.id.login_bttn)
        registerBtn = findViewById(R.id.register)

        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener{
            //Toast.makeText(this, "Login Button pressed", Toast.LENGTH_LONG).show()
            loginBtn.isEnabled = false
            val email: String = emailEt.text.toString()
            val password: String = passwordEt.text.toString()
            if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                Toast.makeText(this@LoginActivity, "Please fill in all the fields", Toast.LENGTH_LONG).show()
                loginBtn.isEnabled = true
            }else{
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful){
                        val user = auth.currentUser;
                        if (user != null) {
                            getUserInfo(user.uid)
                            Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        }

                    }else{
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
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
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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
                        showToast(userName!!, email!!, uid)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }

            })
    }

    private fun showToast(userName: String, email: String, uid: String) {
        Toast.makeText(
            this,
            "Current status \nName: $userName\nEmail: $email\nuid: $uid",
            Toast.LENGTH_LONG
        ).show()
    }

}
