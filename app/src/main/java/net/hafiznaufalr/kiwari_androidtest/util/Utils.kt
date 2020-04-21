package net.hafiznaufalr.kiwari_androidtest.util

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

object DateUtils {
    fun getFormattedTime(timeInMillis: Long): String {
        val date = Date(timeInMillis * 1000L) // *1000 is to convert seconds to milliseconds
        val onlyTime = SimpleDateFormat("h:mm a", Locale.getDefault()) // the format of your date
        val onlyDate = SimpleDateFormat("d MMM", Locale.getDefault()) // the format of your date

        return when {
            isToday(date) -> onlyTime.format(date)
            isYesterday(date) -> "Yesterday"
            else -> onlyDate.format(date)
        }

    }

    fun getFormattedTimeChatLog(timeInMillis: Long): String {
        val date = Date(timeInMillis * 1000L) // *1000 is to convert seconds to milliseconds
        val fullFormattedTime = SimpleDateFormat("d MMM, h:mm a", Locale.getDefault()) // the format of your date
        val onlyTime = SimpleDateFormat("h:mm a", Locale.getDefault()) // the format of your date

        return when {
            isToday(date) -> onlyTime.format(date)
            else -> fullFormattedTime.format(date)
        }

    }

    private fun isYesterday(d: Date): Boolean {
        return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
    }

    private fun isToday(d: Date): Boolean {
        return DateUtils.isToday(d.time)
    }
}