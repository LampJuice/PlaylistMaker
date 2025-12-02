package com.example.playlistmaker.data.sharing

import android.content.Context
import androidx.annotation.PluralsRes
import com.example.playlistmaker.data.ResourceProvider

class ResourceProviderImpl(private val context: Context) : ResourceProvider {
    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }

    override fun getQuantityString(
        @PluralsRes resId: Int,
        quantity: Int,
        vararg args: Any
    ): String {
        return context.resources.getQuantityString(resId, quantity, *args)
    }
}