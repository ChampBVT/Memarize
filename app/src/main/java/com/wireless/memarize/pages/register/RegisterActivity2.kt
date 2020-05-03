package com.wireless.memarize.pages.register

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import com.wireless.memarize.pages.main.MainActivity
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.User
import kotlinx.android.synthetic.main.activity_register_2.*
import java.util.*


class RegisterActivity2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var createBtn: Button
    private lateinit var petName: EditText
    private lateinit var petType: String
    private lateinit var database: FirebaseDatabase
    private lateinit var changeLanguageBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate() // Add (2)
        setContentView(R.layout.activity_register_2)
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.pet_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPet.adapter = adapter
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        createBtn = findViewById(R.id.create)
        petName = findViewById(R.id.petName)
        petType = spinnerPet.selectedItem.toString()

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

        val mBuilder = AlertDialog.Builder(this@RegisterActivity2)
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
            .commit()
    }
}
