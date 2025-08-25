package com.example.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.models.EmailData
import androidx.core.net.toUri

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {
    override fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(intent, null))
    }

    override fun sendEmail(data: EmailData) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(data.email))
            putExtra(Intent.EXTRA_SUBJECT,data.subject)
            putExtra(Intent.EXTRA_TEXT, data.text)
        }
        context.startActivity(intent)
    }

    override fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }


}