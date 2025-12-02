package com.example.playlistmaker.di

import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.FileStorageClient
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.ResourceProvider
import com.example.playlistmaker.data.StorageClient
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.network.ITunesAPI
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.ResourceProviderImpl
import com.example.playlistmaker.data.storage.FileStorageClientImpl
import com.example.playlistmaker.data.storage.PrefsStorageClient
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.models.Song
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    factory { MediaPlayer() }
    single { Gson() }
    single { get<AppDatabase>().songDao() }

    single {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<ITunesAPI> { get<Retrofit>().create(ITunesAPI::class.java) }

    single<StorageClient<Boolean>>(named("themeStorage")) {
        PrefsStorageClient(
            context = androidContext(),
            prefsName = "app_prefs",
            dataKey = "theme_key",
            type = Boolean::class.java,
            gson = get()
        )
    }
    single<StorageClient<List<Song>>>(named("historyStorage")) {
        PrefsStorageClient(
            context = androidContext(),
            prefsName = "app_prefs",
            dataKey = "history_key",
            type = object : TypeToken<List<Song>>() {}.type,
            gson = get()
        )
    }
    single<NetworkClient> { RetrofitNetworkClient(get()) }
    single<ExternalNavigator> { ExternalNavigatorImpl(get()) }
    single<ResourceProvider> { ResourceProviderImpl(get()) }
    factory { (scope: CoroutineScope) -> PlayerInteractorImpl(get(), scope) }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
    single<FileStorageClient> { FileStorageClientImpl(get()) }

    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().savedSongDao() }

}