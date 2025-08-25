package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.ui.settings.view_model.SettingsEvent
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var externalNavigator: ExternalNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        externalNavigator = Creator.provideExternalNavigator(this)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getFactory(
                Creator.provideThemeInteractor(),
                Creator.provideSharingInteractor(this)
            )
        )[SettingsViewModel::class.java]

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