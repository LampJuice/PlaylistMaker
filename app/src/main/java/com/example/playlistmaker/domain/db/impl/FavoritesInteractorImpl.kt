package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavoritesInteractor
import com.example.playlistmaker.domain.db.FavoritesRepository
import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) :
    FavoritesInteractor {
    override suspend fun addFavoriteSong(song: Song) {
        favoritesRepository.addFavoriteSong(song)
    }

    override suspend fun deleteFavoriteSong(song: Song) {
        favoritesRepository.deleteFavoriteSong(song)
    }

    override fun favoriteSongs(): Flow<List<Song>> {

        return favoritesRepository.favoriteSongs()
            .map { songs -> songs.sortedByDescending { it.addedAt } }

    }

    override suspend fun favoriteSongsIds(): List<Int> {
        return favoritesRepository.getSongsIds()
    }
}