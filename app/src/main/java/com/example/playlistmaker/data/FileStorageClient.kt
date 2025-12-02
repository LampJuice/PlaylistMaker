package com.example.playlistmaker.data

interface FileStorageClient {
    suspend fun savePlaylistCover(uriString: String): String?
}