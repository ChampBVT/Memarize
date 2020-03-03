package com.wireless.memarize

import android.R.attr.key
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var createBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        createBtn = findViewById(R.id.create)

        createBtn.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            auth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Successfully Registered as $emailText\nPassword: $passwordText",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this, MainActivity::class.java).apply {
                            val bundle = Bundle()
                            bundle.putString("Email", emailText)
                            bundle.putString("Password", passwordText)
                            bundle.putBoolean("Login", true)
                            putExtra("Bundle", bundle)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

}
