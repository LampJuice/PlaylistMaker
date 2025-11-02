package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun Int.toMinutesAndSeconds(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}

