package com.rsschool.task7_lastfm

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

const val START_TIME = "00:00"
const val BASE_URL = "https://ws.audioscrobbler.com"

fun Int.displayTime(): String {
    if (this <= 0) {
        return START_TIME
    }
    val minutes = this / 60
    val seconds = this % 60

    return "${minutes}:${seconds}"
}

fun String.openInBrowser(context: Context){
    context.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(this)
        )
    )
}

//object SearchingArtists {
//    var isSearching = false
//    var artistName = ""
//}