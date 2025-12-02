package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun Int.toMinutesAndSeconds(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}

fun pluralizeTracks(count: Int): String {
    val rem100 = count % 100
    val rem10 = count % 10

    val ending = when {
        rem100 in 11..14 -> "треков"
        rem10 == 1 -> "трек"
        rem10 in 2..4 -> "трека"
        else -> "треков"
    }

    return "$count $ending"
}

