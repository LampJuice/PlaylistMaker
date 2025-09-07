package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SongsInteractor
import com.example.playlistmaker.domain.search.models.Song
import com.example.playlistmaker.ui.search.mappers.toDomain
import com.example.playlistmaker.ui.search.mappers.toUi
import com.example.playlistmaker.ui.search.models.SongUi
import kotlinx.coroutines.Runnable

class SearchViewModel(
    private val songsInteractor: SongsInteractor,
    private val historyInteractor: SearchHistoryInteractor,
    private val handler: Handler
) : ViewModel() {

    private val searchLiveData = MutableLiveData<SearchUIState>()
    val stateSearchLiveData: LiveData<SearchUIState> = searchLiveData

    private val songs = mutableListOf<Song>()
    var lastQuery: String? = null
    //private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    private var isClickAllowed = true

    fun onFocusGained(text: String, hasFocus: Boolean) {
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
        searchRunnable?.let { handler.removeCallbacks(it) }

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
        searchRunnable = Runnable {
            performSearch(text)
            lastQuery = text
        }
        handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
    }

    fun performSearch(query: String) {

        searchLiveData.postValue(SearchUIState.Loading)
        songsInteractor.searchSongs(query, object : SongsInteractor.SongsConsumer {
            override fun consume(foundSongs: List<Song>) {
                handler.post {

                    songs.clear()
                    songs.addAll(foundSongs)

                    val songsUi = songs.map { it.toUi() }


                    if (songs.isEmpty()) {
                        searchLiveData.value = SearchUIState.ShowEmptyPlaceholder
                    } else {
                        searchLiveData.value =
                            SearchUIState.ShowSearchResults(songsUi.toList())
                    }
                }
            }

            override fun onError(error: Throwable) {
                handler.post { searchLiveData.postValue(SearchUIState.ShowNoNetworkPlaceholder) }
            }

        })
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
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
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

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private val SEARCH_DEBOUNCE_DELAY = 2000L
        private val CLICK_DEBOUNCE_DELAY = 1000L
    }
}