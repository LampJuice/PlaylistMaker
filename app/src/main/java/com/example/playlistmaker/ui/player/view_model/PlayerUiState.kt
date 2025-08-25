package com.example.playlistmaker.ui.player.view_model

import com.example.playlistmaker.domain.player.models.PlayerState

data class PlayerUiState(
    val playerState: PlayerState = PlayerState.DEFAULT,
    val playTime: String = "00:00",
    val isLiked: Boolean = false
) {
}