package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.ui.settings.view_model.SettingsEvent
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val externalNavigator: ExternalNavigator by inject()
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeDarkTheme.observe(this) { isDark ->
            binding.themeSwitcher.apply {
                setOnCheckedChangeListener(null)
                if(isChecked != isDark) {
                    isChecked = isDark
                }
                setOnCheckedChangeListener { _, checked ->
                    viewModel.onThemeSwitch(checked)
                }
            }
        }

        binding.apply {
            settingsBack.setOnClickListener { viewModel.onBackClick() }
            shareIcon.setOnClickListener { viewModel.onShareClick() }
            supportIcon.setOnClickListener { viewModel.onSupportClick() }
            agreementIcon.setOnClickListener { viewModel.onAgreementClick() }
        }

        viewModel.observeEvents.observe(this) { event ->
            when(event) {
                SettingsEvent.Close -> finish()
                is SettingsEvent.ContactSupport -> externalNavigator.sendEmail(event.data)
                is SettingsEvent.OpenAgreement -> externalNavigator.openUrl(event.link)
                is SettingsEvent.ShareApp -> externalNavigator.shareText(event.link)
            }
        }
    }
}