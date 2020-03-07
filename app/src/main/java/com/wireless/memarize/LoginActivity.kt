package com.wireless.memarize

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var registerBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button

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
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
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

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, intent: Intent) {
                val action = intent.action
                if (action == "Close_Login_Activity") {finish()}
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter("Close_Login_Activity"))
    }


}
