package com.example.playlistmaker.ui.mediateka.fragments.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.compose.TrackItem
import com.example.playlistmaker.ui.mediateka.view_model.FavoriteViewModel
import com.example.playlistmaker.ui.mediateka.view_model.FavoritesUiState
import com.example.playlistmaker.ui.search.models.SongUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    onTrackClick: (SongUi) -> Unit,
    viewModel: FavoriteViewModel = koinViewModel()


) {
    val state by viewModel.stateFavoritesLiveData.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.showFavorites()
    }

    when (state) {
        FavoritesUiState.ShowEmptyPlaceholder -> {
            EmptyFavorites()
        }

        is FavoritesUiState.ShowFavorites -> {
            val songs = (state as FavoritesUiState.ShowFavorites).favorites


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.main_padding))
            ) {

                items(songs) { song ->
                    TrackItem(song = song, onTrackClick = { onTrackClick(it) })
                }
            }
        }

        null -> {}
    }


}