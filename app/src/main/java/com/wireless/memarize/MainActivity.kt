package com.wireless.memarize

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var signOutBtn: Button
    private lateinit var textHeader: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textHeader = findViewById(R.id.welcomeHeader)
        signOutBtn = findViewById(R.id.signOut)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser;
        if (user == null) {
            goToLoginIntent()
        } else {
            getUserInfo(user.uid)
            signOutBtn.setOnClickListener {
                auth.signOut()
                goToLoginIntent()
            }
        }
    }

    private fun goToLoginIntent() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getUserInfo(uid: String) {
        val database = FirebaseDatabase.getInstance().reference
        var userName: String = "No Found"
        var email: String = "No Found"
        database.child("users").child(uid).addListenerForSingleValueEvent(
            object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        userName = user.name
                        email = user.email
                        textHeader.text = "Welcome $userName"
                        showToast(userName, email, uid)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }

            })
    }

    private fun showToast(userName:String, email:String, uid: String){
        Toast.makeText(
            this,
            "Current status \nName: $userName\nEmail: $email\nuid: $uid",
            Toast.LENGTH_LONG
        ).show()
    }


}
