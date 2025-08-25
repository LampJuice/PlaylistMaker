package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Song

interface SearchHistoryInteractor {
    fun getHistory(consumer: HistoryConsumer)
    fun saveTrack(track: Song)
    fun clearHistory()
    interface HistoryConsumer{
        fun consume(searchHistory: List<Song>?)
    }
}