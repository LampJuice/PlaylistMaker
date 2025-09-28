package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.ui.settings.view_model.SettingsEvent
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val externalNavigator: ExternalNavigator by inject()
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeDarkTheme.observe(viewLifecycleOwner) { isDark ->
            binding.themeSwitcher.apply {
                setOnCheckedChangeListener(null)
                if (isChecked != isDark) {
                    isChecked = isDark
                }
                setOnCheckedChangeListener { _, checked ->
                    viewModel.onThemeSwitch(checked)
                }
            }
        }

        binding.apply {
            shareIcon.setOnClickListener { viewModel.onShareClick() }
            supportIcon.setOnClickListener { viewModel.onSupportClick() }
            agreementIcon.setOnClickListener { viewModel.onAgreementClick() }
        }

        viewModel.observeEvents.observe(viewLifecycleOwner) { event ->
            when (event) {
                is SettingsEvent.ContactSupport -> externalNavigator.sendEmail(event.data)
                is SettingsEvent.OpenAgreement -> externalNavigator.openUrl(event.link)
                is SettingsEvent.ShareApp -> externalNavigator.shareText(event.link)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}