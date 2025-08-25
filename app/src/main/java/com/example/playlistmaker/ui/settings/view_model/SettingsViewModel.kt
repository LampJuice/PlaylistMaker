package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.settings.ThemeInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.ui.settings.SingleLiveEvent

class SettingsViewModel(
    private val themeInteractor: ThemeInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {


    private val isDarkTheme = MutableLiveData<Boolean>()
    val observeDarkTheme: LiveData<Boolean> = isDarkTheme

    private val events = SingleLiveEvent<SettingsEvent>()
    val observeEvents: LiveData<SettingsEvent> = events

    init {
        isDarkTheme.value = themeInteractor.isDarkTheme()
    }

    fun onBackClick() {
        events.postValue(SettingsEvent.Close)
    }

    fun onThemeSwitch(enabled: Boolean) {
        themeInteractor.setDarkTheme(enabled)
        themeInteractor.applyTheme(enabled)
        isDarkTheme.postValue(enabled)
    }

    fun onShareClick() {
        events.postValue(SettingsEvent.ShareApp(sharingInteractor.getShareAppLink()))
    }

    fun onSupportClick() {
        events.postValue(SettingsEvent.ContactSupport(sharingInteractor.getEmailData()))
    }

    fun onAgreementClick() {
        events.postValue(SettingsEvent.OpenAgreement(sharingInteractor.getAgreementLink()))
    }

    companion object {
        fun getFactory(
            themeInteractor: ThemeInteractor,
            sharingInteractor: SharingInteractor
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(themeInteractor, sharingInteractor)
                }
            }
    }

}