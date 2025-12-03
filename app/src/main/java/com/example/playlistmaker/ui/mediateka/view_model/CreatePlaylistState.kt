package com.example.playlistmaker.ui.mediateka.view_model

data class CreatePlaylistState(
    val name: String = "",
    val description: String = "",
    val coverPath: String? = null,
    val isCreateEnabled: Boolean = false
)
