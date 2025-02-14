package com.wireless.memarize.pages.register

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.content.BroadcastReceiver
import android.content.Context
import android.text.TextUtils
import android.util.Base64
import android.view.TextureView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.wireless.memarize.R
import com.wireless.memarize.utils.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var nextBtn: Button
    private lateinit var name: EditText
    private lateinit var changeLanguageBtn : Button
    private lateinit var backButton: Button

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(arg0: Context, intent: Intent) {
            val action = intent.action
            if (action == "Close_Register_Activity") {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate(this)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        changeLanguageBtn = findViewById(R.id.changeLanguage)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        name = findViewById(R.id.name)
        nextBtn = findViewById(R.id.next)
        backButton = findViewById(R.id.back)

        nextBtn.setOnClickListener {
            nextBtn.isEnabled = false
            if(TextUtils.isEmpty(email.text)||TextUtils.isEmpty(password.text)||TextUtils.isEmpty(name.text)){
                Toast.makeText(this, getString(R.string.fill), Toast.LENGTH_SHORT).show()
                nextBtn.isEnabled = true
            }else if(password.text.length<6) {
                Toast.makeText(this, getString(R.string.minlength), Toast.LENGTH_SHORT).show()
                nextBtn.isEnabled = true
            } else {
                val bundle = Bundle()
                bundle.putString("email", email.text.toString())
                val encode: String = Base64.encodeToString(password.text.toString().toByteArray(), Base64.DEFAULT)
                bundle.putString("password", encode)
                bundle.putString("name", name.text.toString())
                val intent = Intent(this, RegisterActivity2::class.java)
                intent.putExtra("regInfo", bundle)
                startActivity(intent)
                nextBtn.isEnabled = true
            }
        }

        registerReceiver(broadcastReceiver, IntentFilter("Close_Register_Activity"))
        changeLanguageBtn.setOnClickListener {
            displayChangeLanguage(this, this)
        }
        backButton.setOnClickListener{
            onBackPressed();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}
