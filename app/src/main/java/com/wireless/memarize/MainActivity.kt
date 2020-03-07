package com.wireless.memarize

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var signOutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        var login = false
//        var email = "null"
//        var password = "null"
//        val bundle = intent.getBundleExtra("Bundle")
//        if (bundle !== null) {
//            email = bundle.getString("Email", "null")
//            password = bundle.getString("Password", "null")
//            login = bundle.getBoolean("Login", false)
//        }
//        Toast.makeText(
//            this,
//            "Current status\nLogin: $login\nEmail: $email\nPassword: $password",
//            Toast.LENGTH_LONG
//        ).show()
        signOutBtn = findViewById(R.id.signOut)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser;
        if (user == null) { goToLoginIntent() }
        else {
            val email = user.email
            val uid = user.uid
            Toast.makeText(
                this,
                "Current status\nName: AS\nEmail: $email\nPassword: $uid",
                Toast.LENGTH_LONG
            ).show()
            signOutBtn.setOnClickListener {
                auth.signOut()
                goToLoginIntent()
            }
        }
    }

    private fun goToLoginIntent(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}
