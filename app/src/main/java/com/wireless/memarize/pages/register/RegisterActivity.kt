package com.wireless.memarize.pages.register

import android.app.Activity // Add (1) from line 107
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.content.BroadcastReceiver
import android.content.Context
import android.content.res.Configuration
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.wireless.memarize.R
import java.util.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var nextBtn: Button
    private lateinit var name: EditText
    private lateinit var changeLanguageBtn : Button   // Add variable

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
        loadLocate() // Add (2)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        name = findViewById(R.id.name)
        nextBtn = findViewById(R.id.next)

        nextBtn.setOnClickListener {
            //emailRegister(email.text.toString(), password.text.toString())
            nextBtn.isEnabled = false
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

        registerReceiver(broadcastReceiver, IntentFilter("Close_Register_Activity"))

        // Add (3) Change language
        changeLanguageBtn = findViewById(R.id.changeLanguage)

        changeLanguageBtn.setOnClickListener {
            displayChangeLanguage()
        }
        // ------ end (Add 3) -------
    }

    // Add (4) Change language
    private fun displayChangeLanguage() {
        val listLang = arrayOf("EN", "TH")

        val mBuilder = AlertDialog.Builder(this@RegisterActivity)
        mBuilder.setTitle("@string/Select_Language")
        mBuilder.setSingleChoiceItems(listLang, -1)
        { dialog, which ->
            if (which == 0) {
                setLocate("en")
                recreate()
            } else {
                setLocate("th")
                recreate()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun setLocate(language: String?){
        val locale = Locale(language)

        Locale.setDefault(locale)
        val config = Configuration()
        config.locale= locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("myLanguage", language)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("myLanguage", "")
        setLocate(language)
    }
    //------ end (Add 4) -------

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
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
