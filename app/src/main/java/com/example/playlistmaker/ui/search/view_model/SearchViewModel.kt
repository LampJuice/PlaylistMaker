package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.NoNetworkException
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SongsInteractor
import com.example.playlistmaker.domain.search.models.Song
import com.example.playlistmaker.ui.search.mappers.toDomain
import com.example.playlistmaker.ui.search.mappers.toUi
import com.example.playlistmaker.ui.search.models.SongUi
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val songsInteractor: SongsInteractor,
    private val historyInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val searchLiveData = MutableLiveData<SearchUIState>()
    val stateSearchLiveData: LiveData<SearchUIState> = searchLiveData

    private val songs = mutableListOf<Song>()
    var lastQuery: String? = null
    private val trackSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { changedQuery ->
        performSearch(changedQuery)
    }

    private var isClickAllowed = true

    fun onFocusGained(text: String, hasFocus: Boolean) {
        if (!text.isEmpty()) {
            searchLiveData.postValue(SearchUIState.ShowSearchResults(songs.map { it.toUi() }))
            return
        }

        if (text.isEmpty() && hasFocus) {
            historyInteractor.getHistory(object : SearchHistoryInteractor.HistoryConsumer {
                override fun consume(searchHistory: List<Song>?) {
                    if (!searchHistory.isNullOrEmpty()) {
                        val historyUi = searchHistory.map { it.toUi() }
                        searchLiveData.postValue(SearchUIState.ShowHistory(historyUi))
                    } else {
                        searchLiveData.postValue(SearchUIState.Initial)
                    }
                }
            })
        } else if (text.isEmpty()) {
            searchLiveData.postValue(SearchUIState.Initial)
        } else {
            searchLiveData.postValue(SearchUIState.Typing)
        }
    }

    fun onSearchTextChanged(text: String, hasFocus: Boolean) {

        if (text == lastQuery) return

        if (text.isEmpty() && hasFocus) {

            historyInteractor.getHistory(object : SearchHistoryInteractor.HistoryConsumer {
                override fun consume(searchHistory: List<Song>?) {
                    if (!searchHistory.isNullOrEmpty()) {
                        val historyUi = searchHistory.map { it.toUi() }
                        searchLiveData.postValue(SearchUIState.ShowHistory(historyUi))
                    } else {
                        searchLiveData.postValue(SearchUIState.Initial)
                    }
                }
            })
            return
        }

        if (text.isEmpty()) {
            searchLiveData.postValue(SearchUIState.Initial)
            return
        }

        searchLiveData.postValue(SearchUIState.Typing)
        if (lastQuery != text) {
            lastQuery = text
            trackSearchDebounce(text)
        }
    }

    fun performSearch(query: String) {
        searchLiveData.postValue(SearchUIState.Loading)
        viewModelScope.launch {
            try {
                songsInteractor
                    .searchSongs(query)
                    .collect { foundSongs ->
                        songs.clear()
                        songs.addAll(foundSongs)
                        val songUi = foundSongs.map { it.toUi() }
                        if (songs.isEmpty()) {
                            searchLiveData.value = SearchUIState.ShowEmptyPlaceholder
                        } else {
                            searchLiveData.value = SearchUIState.ShowSearchResults(songUi.toList())
                        }
                    }
            } catch (e: NoNetworkException) {
                searchLiveData.value = SearchUIState.ShowNoNetworkPlaceholder
            } catch (e: Exception){
                searchLiveData.value = SearchUIState.ShowEmptyPlaceholder
            }

        }
    }

    fun onRenewClick() {
        searchLiveData.postValue(SearchUIState.Loading)
        lastQuery?.let { performSearch(it) }
    }

    fun onTrackClick(song: SongUi): Boolean {
        if (!clickDebounce()) return false
        historyInteractor.saveTrack(song.toDomain())
        return true
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (current) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        searchLiveData.postValue(SearchUIState.Initial)
    }

    fun clearEditText() {
        searchLiveData.value = SearchUIState.ClearInput
    }


    companion object {
        private val SEARCH_DEBOUNCE_DELAY = 2000L
        private val CLICK_DEBOUNCE_DELAY = 1000L
    }
}