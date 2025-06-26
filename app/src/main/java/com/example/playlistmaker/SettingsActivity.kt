package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.settings_back)
        val shareButton = findViewById<ImageView>(R.id.share_icon)
        val supportButton = findViewById<ImageView>(R.id.support_icon)
        val agreementButton = findViewById<ImageView>(R.id.agreement_icon)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val app = applicationContext as App

        themeSwitcher.isChecked = app.darkTheme

        backButton.setOnClickListener {
            finish()
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            app.switchTheme(checked)
        }

        shareButton.setOnClickListener {
            val url = getString(R.string.android_dev)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
            shareIntent.type = "text/plain"
            startActivity(Intent.createChooser(shareIntent, getString(R.string.android_dev1)))
        }
        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_xtra)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_xtra))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.text_xtra))

                startActivity(this)
            }
        }
        agreementButton.setOnClickListener {
            val agreementIntent = Intent(
                Intent.ACTION_VIEW,
                getString(R.string.user_agreement_url).toUri()
            ).apply {
                startActivity(this)
            }
        }

    }
}