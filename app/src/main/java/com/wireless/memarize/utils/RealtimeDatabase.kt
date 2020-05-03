package com.wireless.memarize.utils

import android.content.Context
import com.google.firebase.database.FirebaseDatabase

private var database = FirebaseDatabase.getInstance()

fun setRealtimeDatabaseValue( string : String, value : Any, context : Context){
    val databaseRef = database.reference
    val uid = getEncryptedSharePreferencesString("uid", context)
    databaseRef.child("users").child(uid).child(string).setValue(value)
}

fun updateRealtimeDatabaseValue( string : String, value : Any, context : Context){
    val databaseRef = database.reference
    val uid = getEncryptedSharePreferencesString("uid", context)
    val updateValue = mutableMapOf<String,Any>("test" to "pizza")
    databaseRef.child("users").child(uid).updateChildren(updateValue)
}

fun removeRealtimeDatabaseValue( string : String, context : Context){
    val databaseRef = database.reference
    val uid = getEncryptedSharePreferencesString("uid", context)
    databaseRef.child("users").child(uid).child(string).removeValue()
}
