package com.example.playlistmaker.ui.settings.view_model

import com.example.playlistmaker.domain.sharing.models.EmailData

sealed class SettingsEvent {
    object Close : SettingsEvent()
    data class ShareApp(val link: String) : SettingsEvent()
    data class ContactSupport(val data: EmailData) : SettingsEvent()
    data class OpenAgreement(val link: String) : SettingsEvent()
}