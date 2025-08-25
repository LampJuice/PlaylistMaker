package com.example.playlistmaker.creator

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.player.PlayerTimer
import com.example.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.SongsRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.ThemeRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.ResourceProviderImpl
import com.example.playlistmaker.data.storage.PrefsStorageClient
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.SongsInteractor
import com.example.playlistmaker.domain.search.SongsRepository
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SongsInteractorImpl
import com.example.playlistmaker.domain.search.models.Song
import com.example.playlistmaker.domain.settings.ThemeInteractor
import com.example.playlistmaker.domain.settings.ThemeRepository
import com.example.playlistmaker.domain.settings.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.google.gson.reflect.TypeToken

@SuppressLint("StaticFieldLeak")
object Creator {

    private lateinit var context: Context


    fun init(appContext: Context) {
        context = appContext.applicationContext
    }


    private fun getSongsRepository(): SongsRepository {
        return SongsRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSongsInteractor(): SongsInteractor {
        return SongsInteractorImpl(getSongsRepository())
    }


    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient<ArrayList<Song>>(
                context, "SONG_SEARCH", "HISTORY", object : TypeToken<ArrayList<Song>>() {}.type
            )
        )

    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private fun getThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(
            PrefsStorageClient<Boolean>(
                context, "THEME_PREFS", "dark_theme", Boolean::class.java
            )
        )
    }

    fun provideThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository())
    }

    private val mediaPlayer: MediaPlayer by lazy {
        MediaPlayer()
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(mediaPlayer)
    }

    fun providePlayerInteractor(): PlayerInteractor {
        val repository = getPlayerRepository()
        val timer = PlayerTimer(repository)
        return PlayerInteractorImpl(repository, timer)
    }

    fun provideResourceProvider(context: Context) = ResourceProviderImpl(context)

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            resourceProvider = provideResourceProvider(context)
        )
    }

    fun provideExternalNavigator(context: Context): ExternalNavigator =
        ExternalNavigatorImpl(context)

}