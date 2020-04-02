package com.wireless.memarize

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var nextBtn: Button
    private lateinit var name: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        name = findViewById(R.id.name)
        nextBtn = findViewById(R.id.next)

        nextBtn.setOnClickListener {
            //emailRegister(email.text.toString(), password.text.toString())
            val bundle = Bundle()
            bundle.putString("email", email.text.toString())
            val encode: String = Base64.encodeToString(password.text.toString().toByteArray(), Base64.DEFAULT)
            bundle.putString("password", encode)
            bundle.putString("name", name.text.toString())
            val intent = Intent(this, RegisterActivity2::class.java)
            intent.putExtra("regInfo", bundle)
            startActivity(intent)
        }
    }

//    private fun emailRegister(emailText:String, passwordText:String){
//        createBtn.isEnabled = false
//        if(TextUtils.isEmpty(emailText)|| TextUtils.isEmpty(passwordText)){
//            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show()
//            createBtn.isEnabled = true
//        } else {
//            auth.createUserWithEmailAndPassword(emailText, passwordText)
//                .addOnCompleteListener(this, OnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        createUser(name.text.toString(),emailText)
//                        Toast.makeText(
//                            this,
//                            "Successfully Registered as $emailText\nPassword: $passwordText",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        val intent = Intent(this, MainActivity::class.java)
//                        val closeLoginActivity = Intent("Close_Login_Activity")
//                        sendBroadcast(closeLoginActivity)
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        Log.d("test", "createUserWithEmail:failure", task.exception)
//                        Toast.makeText(
//                            this, "Failed: " + (task.exception?.message
//                                ?: "No error message"), Toast.LENGTH_SHORT
//                        ).show();
//                        createBtn.isEnabled = true
//                    }
//                })
//        }
//    }
//
//    private fun createUser(name:String, email:String){
//        val database = FirebaseDatabase.getInstance().reference
//        val user = User(name, email)
//        database.child("users").child(auth.currentUser?.uid.toString()).setValue(user)
//            .addOnCompleteListener(this, OnCompleteListener { task ->
//            Toast.makeText(
//                this, "Failed: " + (task.exception?.message
//                    ?: "No error message"), Toast.LENGTH_SHORT
//            ).show();
//        });
//    }
}
