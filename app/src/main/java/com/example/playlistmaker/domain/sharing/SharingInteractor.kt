package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.models.EmailData

interface SharingInteractor {
    fun getShareAppLink() : String
    fun getAgreementLink() : String
    fun getEmailData() : EmailData
}