package com.wireless.memarize.pages.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.wireless.memarize.pages.main.MainActivity
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.User
import com.wireless.memarize.utils.*


class RegisterActivity2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var createBtn: Button
    private lateinit var petName: EditText
    private lateinit var petType: String
    private lateinit var database: FirebaseDatabase
    private lateinit var changeLanguageBtn : Button
    private lateinit var spinnerPet: Spinner
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate(this)
        setContentView(R.layout.activity_register_2)
        spinnerPet = findViewById(R.id.spinnerPet)
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.pet_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPet.adapter = adapter
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        createBtn = findViewById(R.id.create)
        petName = findViewById(R.id.petName)
        changeLanguageBtn = findViewById(R.id.changeLanguage)
        backButton = findViewById(R.id.back)

        changeLanguageBtn.setOnClickListener {
            displayChangeLanguage(this, this)
        }

        backButton.setOnClickListener{
            onBackPressed();
        }

        spinnerPet.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                petType = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                petType = parent.getItemAtPosition(0) as String
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val bundle = intent.getBundleExtra("regInfo")
        var email: String? = null
        val password: ByteArray
        var decodedPassword: String? = null
        var name: String? = null
        if (bundle !== null) {
            email = bundle.getString("email", null)
            password = Base64.decode(bundle.getString("password", null), Base64.DEFAULT)
            decodedPassword = password?.let { String(password) }
            name = bundle.getString("name", null)
        }
        createBtn.setOnClickListener {
            if (email != null && decodedPassword != null && name != null) {
                emailRegister(email, decodedPassword, name, petName.text.toString(), petType)
            }
        }
    }

    private fun emailRegister(emailText:String, passwordText:String, nameText:String, petNameText:String, petTypeText:String){
        createBtn.isEnabled = false
        if(TextUtils.isEmpty(emailText)|| TextUtils.isEmpty(passwordText)){
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show()
            createBtn.isEnabled = true
        } else {
            auth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        createUser(nameText, emailText, petNameText, petTypeText)
                        Toast.makeText(
                            this,
                            "Successfully Registered",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this, MainActivity::class.java)
                        val closeLoginActivity = Intent("Close_Login_Activity")
                        val closeRegisterActivity = Intent("Close_Register_Activity")
                        sendBroadcast(closeLoginActivity)
                        sendBroadcast(closeRegisterActivity)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.d("test", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this, "Failed: " + (task.exception?.message
                                ?: "No error message"), Toast.LENGTH_SHORT
                        ).show();
                        createBtn.isEnabled = true
                    }
                })
        }
    }

    private fun createUser(name:String, email:String, petName:String, petType: String){
        val databaseRef = database.reference
        val user = User(name, email, petName, petType, 0)
        val uid = auth.currentUser?.uid.toString()
        databaseRef.child("users").child(uid).setValue(user)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                Toast.makeText(
                    this, "Failed: " + (task.exception?.message
                        ?: "No error message"), Toast.LENGTH_SHORT
                ).show();
            });
        setEncryptedSharePreferences(name, email, uid, petName, petType, 0, this)
    }
}
