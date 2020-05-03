package com.wireless.memarize.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

@SuppressLint("ApplySharedPref")
fun setEncryptedSharePreferencesString(string : String, value : String, context : Context) {
    val sharedPreferences = EncryptedSharedPreferences.create(
        "PreferencesFilename",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    sharedPreferences.edit()
        .putString(string, value)
        .commit()
}

@SuppressLint("ApplySharedPref")
fun setEncryptedSharePreferencesLong(string : String, value : Long, context : Context) {
    val sharedPreferences = EncryptedSharedPreferences.create(
        "PreferencesFilename",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    Log.e("setCoins", value.toString())
    sharedPreferences.edit()
        .putLong(string, value)
        .commit()
}

fun getEncryptedSharePreferencesString(string : String, context : Context): String {
    val sharedPreferences = EncryptedSharedPreferences.create(
        "PreferencesFilename",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    return sharedPreferences.getString(string, "null") as String
}

fun getEncryptedSharePreferencesLong(string : String, context : Context): Long {
    val sharedPreferences = EncryptedSharedPreferences.create(
        "PreferencesFilename",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    return sharedPreferences.getLong(string, -1)
}

@SuppressLint("ApplySharedPref")
fun setEncryptedSharePreferences(userName: String, email: String, uid: String, petName: String, petType: String, coins: Long, context : Context) {
    val sharedPreferences = EncryptedSharedPreferences.create(
        "PreferencesFilename",
        masterKeyAlias,
        context,
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