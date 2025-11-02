package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.SearchRequest
import com.example.playlistmaker.data.search.dto.SearchResponse
import com.example.playlistmaker.domain.search.SongsRepository
import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SongsRepositoryImpl(private val networkClient: NetworkClient) : SongsRepository {
    override fun searchSongs(expression: String): Flow<List<Song>> = flow {
        val response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            with(response as SearchResponse) {
                val data = results.map {
                    Song(
                        it.trackName ?: "",
                        it.artistName ?: "",
                        it.trackTimeMillis ?: 0L,
                        it.artworkUrl100 ?: "",
                        it.trackId ?: 0,
                        it.collectionName ?: "",
                        it.releaseDate ?: "",
                        it.country ?: "",
                        it.primaryGenreName ?: "",
                        it.previewUrl ?: ""
                    )
                }
                emit(data)
            }
        } else {
            emit(emptyList())
        }
    }
}