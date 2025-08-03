package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.SongsRepositoryImpl
import com.example.playlistmaker.data.ThemeRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.SongsInteractor
import com.example.playlistmaker.domain.api.SongsRepository
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.ThemeRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SongsInteractorImpl
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl

@SuppressLint("StaticFieldLeak")
object Creator {

    private lateinit var context: Context
    private const val PREFS_THEME = "theme_prefs"
    private const val PREFS_HISTORY = "history_prefs"


    fun init(appContext: Context){
        context = appContext.applicationContext
    }



    private fun getSongsRepository(): SongsRepository {
        return SongsRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSongsInteractor(): SongsInteractor {
        return SongsInteractorImpl(getSongsRepository())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        val prefs = context.getSharedPreferences(PREFS_HISTORY, Context.MODE_PRIVATE)
        return SearchHistoryRepositoryImpl(prefs)
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    private fun getThemeRepository(): ThemeRepository{
        val prefs = context.getSharedPreferences(PREFS_THEME, Context.MODE_PRIVATE)
        return ThemeRepositoryImpl(prefs)
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
        return PlayerInteractorImpl(getPlayerRepository())
    }

}