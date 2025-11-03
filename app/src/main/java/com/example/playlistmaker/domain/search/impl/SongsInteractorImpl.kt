package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SongsInteractor
import com.example.playlistmaker.domain.search.SongsRepository
import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow

class SongsInteractorImpl(private val repository: SongsRepository) :
    SongsInteractor {

    override fun searchSongs(
        expression: String
    ): Flow<List<Song>> {
        return repository.searchSongs(expression)
    }
}