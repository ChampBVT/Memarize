package com.wireless.memarize.utils

import android.content.Context
import com.google.firebase.database.FirebaseDatabase

private var database = FirebaseDatabase.getInstance()

fun setRealtimeDatabaseValue( string : String, value : Any, context : Context){
    val databaseRef = database.reference
    val uid = getEncryptedSharePreferencesString("uid", context)
    databaseRef.child("users").child(uid).child(string).setValue(value)
}
