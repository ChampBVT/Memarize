package com.wireless.memarize

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register_2.*


class RegisterActivity2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var createBtn: Button
    private lateinit var petName: EditText
    private lateinit var petType: String
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_2)
        val adapter = ArrayAdapter.createFromResource(this, R.array.pet_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPet.adapter = adapter
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        createBtn = findViewById(R.id.create)
        petName = findViewById(R.id.petName)
        petType = spinnerPet.selectedItem.toString()
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
                            "Successfully Registered as $emailText\nPassword: $passwordText",
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
        setEncryptedSharePreferences(name, email, uid, petName, petType, 0)
    }

    private fun setEncryptedSharePreferences(userName: String, email: String, uid: String, petName: String, petType: String, coins: Long) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "PreferencesFilename",
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.edit()
            .putString("userName", userName)
            .putString("email", email)
            .putString("uid", uid)
            .putString("petName", petName)
            .putString("petType", petType)
            .putLong("coins", coins)
            .apply()
    }
}
