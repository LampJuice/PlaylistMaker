package com.example.playlistmaker.ui.mediateka.view_model

import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.search.models.SongUi

sealed class PlaylistScrState {
    object Loading : PlaylistScrState()
    data class Content(
        val playlist: Playlist,
        val song: List<SongUi>,
        val duration: String,
        val trackCount: Int
    ) : PlaylistScrState()

    object Error : PlaylistScrState()
}
