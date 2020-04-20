package net.hafiznaufalr.kiwari_androidtest.data

import android.os.Parcelable

data class User(
    var uid: String? = null,
    var avatar: String? = null,
    var email: String? = null,
    var name: String? = null,
    var password: String? = null
)