package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.ACTION_VIEW
import android.content.Intent.EXTRA_EMAIL
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.content.Intent.createChooser
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeInteractor: ThemeInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        themeInteractor = Creator.provideThemeInteractor()

        val backButton = findViewById<ImageView>(R.id.settings_back)
        val shareButton = findViewById<ImageView>(R.id.share_icon)
        val supportButton = findViewById<ImageView>(R.id.support_icon)
        val agreementButton = findViewById<ImageView>(R.id.agreement_icon)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.isChecked = themeInteractor.isDarkTheme()
        val app = applicationContext as App

        backButton.setOnClickListener {
            finish()
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            app.switchTheme(checked)

        }

        shareButton.setOnClickListener {
            val url = getString(R.string.android_dev)
            val shareIntent = Intent(ACTION_SEND)
            shareIntent.putExtra(EXTRA_TEXT, url)
            shareIntent.type = "text/plain"
            startActivity(createChooser(shareIntent, getString(R.string.android_dev1)))
        }
        supportButton.setOnClickListener {
            val supportIntent = Intent(ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(EXTRA_EMAIL, arrayOf(getString(R.string.email_xtra)))
                putExtra(EXTRA_SUBJECT, getString(R.string.subject_xtra))
                putExtra(EXTRA_TEXT, getString(R.string.text_xtra))

                startActivity(this)
            }
        }
        agreementButton.setOnClickListener {
            val agreementIntent = Intent(
                ACTION_VIEW,
                getString(R.string.user_agreement_url).toUri()
            ).apply {
                startActivity(this)
            }
        }

    }
}