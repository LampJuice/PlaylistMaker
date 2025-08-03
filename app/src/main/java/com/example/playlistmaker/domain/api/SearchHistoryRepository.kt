package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Song

interface SearchHistoryRepository {
    fun getHistory() : List<Song>
    fun saveTrack(track: Song)
    fun clearHistory()
}