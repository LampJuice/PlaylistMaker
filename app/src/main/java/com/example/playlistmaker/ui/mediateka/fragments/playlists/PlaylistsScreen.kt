package com.example.playlistmaker.ui.mediateka.fragments.playlists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.compose.PlaylistItem
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistsState
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsScreen(
    onPlaylistClick: (Playlist) -> Unit,
    onCreatePlaylistClick: () -> Unit,
    viewModel: PlaylistsViewModel = koinViewModel()
) {
    val state by viewModel.state.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onCreatePlaylistClick,
            colors = ButtonDefaults.buttonColors(
                colorResource(R.color.text_b_s_pl),
                colorResource(R.color.background_menu)
            ),
            modifier = Modifier
                .padding(top = 24.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                stringResource(R.string.playlist_new),
                color = colorResource(R.color.background_menu)
            )
        }

        when (state) {
            is PlaylistsState.Content -> {
                val playlists = (state as PlaylistsState.Content).playlists

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp)

                ) {
                    items(playlists) { playlist ->
                        PlaylistItem(
                            playlist = playlist,
                            onClick = { onPlaylistClick(playlist) }
                        )
                    }
                }
            }

            is PlaylistsState.Empty -> {
                EmptyPlaylists()
            }

            null -> {}
        }

    }
}