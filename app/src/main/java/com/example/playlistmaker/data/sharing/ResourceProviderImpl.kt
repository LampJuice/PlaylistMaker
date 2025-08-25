package com.example.playlistmaker.data.sharing

import android.content.Context
import com.example.playlistmaker.data.ResourceProvider

class ResourceProviderImpl(private val context: Context): ResourceProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}