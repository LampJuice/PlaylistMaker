package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val externalNavigator: ExternalNavigator by inject()
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SettingsScreen(
                    viewModel = viewModel,
                    onShare = { link ->
                        externalNavigator.shareText(link)
                    },
                    onSupport = { data ->
                        externalNavigator.sendEmail(data)
                    },
                    onAgreement = { link ->
                        externalNavigator.openUrl(link)
                    }
                )
            }
        }
    }
}