package com.example.playlistmaker.data

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes resId: Int, vararg args: Any): String
    fun getQuantityString(@PluralsRes resId: Int, quantity: Int, vararg args: Any): String
}