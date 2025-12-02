package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.data.FileStorageClient
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository,
    private val fileStorage: FileStorageClient
) : PlaylistInteractor {


    override suspend fun addTrackToPlaylist(
        playlistId: Int,
        song: Song
    ): Boolean {
        val playlistEntity = repository.getPlaylistById(playlistId) ?: return false
        val currentIds = playlistEntity.songIds.toMutableList()
        if (currentIds.contains(song.trackId)) return false
        currentIds.add(song.trackId)
        repository.saveSongPlaylist(song)
        repository.updatePlaylist(
            playlistEntity.copy(
                songIds = currentIds,
                songCount = currentIds.size
            )
        )

        return true
    }

    override suspend fun createPlaylist(
        name: String,
        description: String?,
        coverPath: String?
    ): Int {

        val newPlaylist = Playlist(
            name = name,
            description = description,
            coverPath = coverPath,
            songIds = emptyList(),
            songCount = 0
        )

        return repository.addPlaylist(newPlaylist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun getPlayListById(id: Int): Playlist? {
        return repository.getPlaylistById(id)
    }

    override suspend fun saveCover(uriString: String): String? {
        return fileStorage.savePlaylistCover(uriString)
    }
}