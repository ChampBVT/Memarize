package com.wireless.memarize

data class User(
    var name: String = "",
    var email: String = "",
    var petName: String = "",
    var petType: String = "",
    var coins: Long = 0,
    var progress: String =""
)