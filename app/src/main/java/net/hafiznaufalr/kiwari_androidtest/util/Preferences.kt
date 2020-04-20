@file:Suppress("DEPRECATION")

package net.hafiznaufalr.kiwari_androidtest.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import net.hafiznaufalr.kiwari_androidtest.data.User

object Preferences {
    private const val KEY_STATUS = "Status_logged_in"
    private const val KEY_USER = "User_data"

    private fun getSharedPreference(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun putUser(context: Context, json: String) {
        val editor = getSharedPreference(context).edit()
        editor.putString(KEY_USER, json)
        editor.apply()
    }

    fun getUser(context: Context): User? {
        return try {
            Gson().fromJson<User>(getSharedPreference(context).getString(KEY_USER, null), User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun putStatus(context: Context, status: Boolean) {
        val editor = getSharedPreference(context).edit()
        editor.putBoolean(KEY_STATUS, status)
        editor.apply()
    }

    fun getStatus(context: Context): Boolean {
        return getSharedPreference(context).getBoolean(KEY_STATUS, false)
    }

    fun clearUser(context: Context) {
        val editor = getSharedPreference(context).edit()
        editor.remove(KEY_USER)
        editor.remove(KEY_STATUS)
        editor.apply()
    }


}