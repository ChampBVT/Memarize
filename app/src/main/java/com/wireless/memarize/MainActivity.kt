package com.wireless.memarize

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var login = false
        var email = "null"
        var password = "null"
        val bundle = intent.getBundleExtra("Bundle")
        if (bundle !== null) {
            email = bundle.getString("Email", "null")
            password = bundle.getString("Password", "null")
            login = bundle.getBoolean("Login", false)
        }
        Toast.makeText(
            this,
            "Current status\nLogin: $login\nEmail: $email\nPassword: $password",
            Toast.LENGTH_LONG
        ).show()
        if (!login) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
