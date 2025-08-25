package com.example.playlistmaker.domain.search

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.domain.search.models.Song

interface SearchHistoryRepository {
    fun getHistory() : Resource<List<Song>>
    fun saveTrack(track: Song)
    fun clearHistory()
}