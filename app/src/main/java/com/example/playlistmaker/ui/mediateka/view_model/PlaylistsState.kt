package com.example.playlistmaker.ui.mediateka.view_model

import com.example.playlistmaker.domain.playlist.models.Playlist

sealed interface PlaylistsState {
    object Empty : PlaylistsState
    data class Content(val playlists: List<Playlist>) : PlaylistsState
}