package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Song

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun getHistory(consumer: SearchHistoryInteractor.HistoryConsumer){
        consumer.consume(repository.getHistory().data)
    }


    override fun saveTrack(track: Song) = repository.saveTrack(track)


    override fun clearHistory() = repository.clearHistory()

}