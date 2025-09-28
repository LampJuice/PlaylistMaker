package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.R
import com.example.playlistmaker.data.ResourceProvider
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.models.EmailData

class SharingInteractorImpl(private val resourceProvider: ResourceProvider) : SharingInteractor {

    override fun getShareAppLink(): String {
        return resourceProvider.getString(R.string.android_dev)
    }

    override fun getAgreementLink(): String {
        return resourceProvider.getString(R.string.user_agreement_url)
    }

    override fun getEmailData(): EmailData {
        return EmailData(
            email = resourceProvider.getString(R.string.email_xtra),
            subject = resourceProvider.getString(R.string.subject_xtra),
            text = resourceProvider.getString(R.string.text_xtra)

        )
    }
}