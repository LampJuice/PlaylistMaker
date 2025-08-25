package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.models.EmailData

interface ExternalNavigator {
    fun shareText(text: String)
    fun sendEmail(data: EmailData)
    fun openUrl(url: String)
}