package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.SearchRequest
import com.example.playlistmaker.data.dto.SearchResponse
import com.example.playlistmaker.domain.api.SongsRepository
import com.example.playlistmaker.domain.models.Song
import com.example.playlistmaker.toMinutesAndSeconds

class SongsRepositoryImpl(private val networkClient: NetworkClient): SongsRepository {
    override fun searchSongs(expression: String): List<Song> {
        val  response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as SearchResponse).results.map {
                val time = it.trackTimeMillis?.toMinutesAndSeconds() ?: "00:00"
                Song(
                    it.trackName,
                    it.artistName,
                    time,
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.country,
                    it.primaryGenreName,
                    it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}