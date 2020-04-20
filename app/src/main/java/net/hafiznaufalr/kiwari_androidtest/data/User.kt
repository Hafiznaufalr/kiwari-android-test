package net.hafiznaufalr.kiwari_androidtest.data

import android.os.Parcelable

data class User(
    val uid: String,
    val avatar: String,
    val email: String,
    val name: String,
    val password: String
)