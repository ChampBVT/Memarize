package com.wireless.memarize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
        createBtn = findViewById(R.id.createButton)
        createBtn.setOnClickListener {}
        auth.createUserWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener(this, OnCompleteListener{ task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Successfully Registered as $email and $password", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

}
