package com.example.playlistmaker.ui.mediateka.fragments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.compose.ScreenTitle
import com.example.playlistmaker.ui.mediateka.fragments.favorite.FavoritesScreen
import com.example.playlistmaker.ui.mediateka.fragments.playlists.PlaylistsScreen
import com.example.playlistmaker.ui.search.models.SongUi
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediatekaScreen(
    onTrackClick: (SongUi) -> Unit,
    onPlaylistClick: (Playlist) -> Unit,
    onCreatePlaylistClick: () -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_menu))

    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.main_padding))
        ) {
            ScreenTitle(title = stringResource(R.string.media))
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPosition ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPosition[pagerState.currentPage])
                        .padding(horizontal = dimensionResource(R.dimen.main_padding)),
                    color = colorResource(R.color.text_menu)
                )
            },
            divider = {}
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                text = {
                    Text(
                        stringResource(R.string.tab_favorites),
                        color = colorResource(R.color.text_menu)
                    )
                },
                modifier = Modifier
                    .background(colorResource(R.color.background_menu))

            )
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                text = {
                    Text(
                        stringResource(R.string.tab_playlists),
                        color = colorResource(R.color.text_menu)
                    )
                },
                modifier = Modifier.background(colorResource(R.color.background_menu))

            )
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> FavoritesScreen(onTrackClick = onTrackClick)
                1 -> PlaylistsScreen(
                    onPlaylistClick = onPlaylistClick,
                    onCreatePlaylistClick = onCreatePlaylistClick
                )

            }
        }
    }
}