package com.example.playlistmaker.ui.search.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.compose.ScreenTitle
import com.example.playlistmaker.ui.compose.TrackItem
import com.example.playlistmaker.ui.search.models.SongUi
import com.example.playlistmaker.ui.search.view_model.SearchUIState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onTrackClick: (SongUi) -> Unit
) {
    val state by viewModel.stateSearchLiveData.observeAsState(SearchUIState.Initial)

    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    var query by rememberSaveable { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadHistoryOnStart()
    }

    val isClearEvent = state is SearchUIState.ClearInput

    LaunchedEffect(isClearEvent) {
        if (isClearEvent) {
            query = ""
            focusManager.clearFocus()
            keyboard?.hide()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_menu))
            .padding(horizontal = dimensionResource(R.dimen.main_padding))
    ) {
        ScreenTitle(title = stringResource(R.string.search))


        SearchField(
            query = query,
            onQueryChange = {
                query = it
                viewModel.onSearchTextChanged(it, isFocused)
            },
            onClearClick = { viewModel.clearEditText() },
            onFocusChanged = {
                isFocused = it
                viewModel.onFocusGained(query, it)
            },
            focusRequester = focusRequester

        )

        when (val s = state) {
            is SearchUIState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }


            is SearchUIState.ShowEmptyPlaceholder -> {
                EmptyPlaceholder(R.drawable.ic_no_result, R.string.no_result)
            }

            is SearchUIState.ShowHistory -> {
                HistoryBlock(s.history, onTrackClick) { viewModel.clearHistory() }
            }

            is SearchUIState.ShowNoNetworkPlaceholder -> {
                NoNetworkPlaceholder { viewModel.onRenewClick() }
            }

            is SearchUIState.ShowSearchResults -> {
                TrackList(s.results, onTrackClick)
            }

            else -> {}
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    focusRequester: FocusRequester
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.icon_padding))

    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier

                .fillMaxWidth()

                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChanged(it.isFocused) },
            singleLine = true,

            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    stringResource(R.string.search),
                    fontSize = dimensionResource(R.dimen.text_regular).value.sp,
                    color = colorResource(R.color.search_hint)
                )
            },
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.ic_search_16),
                    contentDescription = null,
                    tint = colorResource(R.color.search_hint)
                )
            },

            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClearClick) {
                        Icon(
                            painterResource(R.drawable.ic_close_16),
                            contentDescription = null,
                            tint = colorResource(R.color.search_hint)
                        )
                    }
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = colorResource(R.color.search_field),
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,


                )


        )
    }
}

@Composable
fun TrackList(
    songs: List<SongUi>,
    onTrackClick: (SongUi) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(top = 24.dp)) {
        items(songs) { song ->
            TrackItem(song, onTrackClick)
        }
    }
}


@Composable
fun HistoryBlock(
    history: List<SongUi>,
    onTrackClick: (SongUi) -> Unit,
    onClear: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            stringResource(R.string.history),
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            color = colorResource(R.color.text_b_s_pl),
            modifier = Modifier.padding(top = 50.dp, bottom = 20.dp),

            )

        LazyColumn(
            modifier = Modifier.weight(1f, fill = false)
        ) {
            items(history) { song ->
                TrackItem(song, onTrackClick)
            }
        }


        Button(
            onClick = onClear,
            colors = ButtonDefaults.buttonColors(
                colorResource(R.color.text_b_s_pl),
                colorResource(R.color.background_menu)
            ),
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        ) {
            Text(stringResource(R.string.history_clear))
        }
    }
}

@Composable
fun EmptyPlaceholder(icon: Int, text: Int) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(icon), null)
        Text(stringResource(text))
    }
}

@Composable
fun NoNetworkPlaceholder(onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(R.drawable.ic_no_network), null)
        Text(stringResource(R.string.no_network))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.renew))
        }
    }
}