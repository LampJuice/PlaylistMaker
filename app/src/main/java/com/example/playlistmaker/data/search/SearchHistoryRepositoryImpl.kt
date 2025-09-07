package com.example.playlistmaker.data.search

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.data.StorageClient
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Song

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Song>>
) : SearchHistoryRepository {

    override fun getHistory(): Resource<List<Song>> {
        val songs = storage.getData() ?: listOf()
        return Resource.Success(songs)
    }

    override fun saveTrack(track: Song) {
        val songs = storage.getData() ?: arrayListOf()
        songs.removeAll { it.trackId == track.trackId }
        songs.add(0, track)
        if (songs.size > MAX_HISTORY_SIZE) {
            songs.removeAt(songs.size - 1)
        }
        storage.storeData(songs)
    }

    override fun clearHistory() {
        storage.clearData()
    }

    companion object {
        private const val MAX_HISTORY_SIZE = 10
    }
}