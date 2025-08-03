package com.example.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SongsInteractor
import com.example.playlistmaker.domain.models.Song
import com.example.playlistmaker.ui.player.PlayerActivity
import com.google.android.material.button.MaterialButton

class SearchActivity : AppCompatActivity() {
    var searchString: String = STRING_DEF
    private var lastSearch: String? = null

    private lateinit var searchHistory: SearchHistoryInteractor

    private val interactor = Creator.provideSongsInteractor()

    private val songs = mutableListOf<Song>()
    private val trackAdapter = TrackAdapter(songs) { song ->
        if (clickDebounce()) {
            searchHistory.saveTrack(song)
            openPlayer(song)
        }
    }
    private val historyAdapter =
        TrackAdapter(mutableListOf()) { song ->
            if (clickDebounce()) {
                searchHistory.saveTrack(song)
                openPlayer(song)
            }
        }
    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable = Runnable { performSearch(lastSearch!!) }

    private lateinit var noNetworkPlaceholder: LinearLayout
    private lateinit var noResultPlaceholder: LinearLayout
    private lateinit var historyHint: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        searchHistory = Creator.provideSearchHistoryInteractor()

        val backButton = findViewById<ImageView>(R.id.search_back)
        val editText = findViewById<EditText>(R.id.search_edittext)
        val clearButton = findViewById<ImageView>(R.id.clear_text)
        historyHint = findViewById<LinearLayout>(R.id.history_hint)
        progressBar = findViewById<ProgressBar>(R.id.search_progress_bar)

        val clearHistoryButton = findViewById<MaterialButton>(R.id.clear_history_button)


        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            historyAdapter.updateData(emptyList())
            historyHint.visibility = View.GONE
        }

        backButton.setOnClickListener { finish() }

        editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && editText.text.isEmpty()) {
                val history = searchHistory.getHistory()
                if (history.isNotEmpty()) {
                    historyAdapter.updateData(history)
                    historyHint.visibility = View.VISIBLE
                } else {
                    historyHint.visibility = View.GONE
                }
            } else {
                historyHint.visibility = View.GONE
            }
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isEmpty = s.isNullOrEmpty()
                val hasFocus = editText.hasFocus()
                val history = searchHistory.getHistory()
                if (hasFocus) {
                    when {
                        isEmpty && history.isNotEmpty() -> renderState(SearchUIState.ShowHistory)
                        isEmpty -> renderState(SearchUIState.Initial)
                        else -> {
                            renderState(SearchUIState.Typing)
                        }
                    }
                }
                clearButton.visibility = if (isEmpty) View.INVISIBLE else View.VISIBLE

                searchHandler.removeCallbacks(searchRunnable)
                searchRunnable = Runnable {
                    if (!s.isNullOrEmpty()) {
                        lastSearch = s.toString()
                        performSearch(s.toString())
                    }
                }
                searchHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)

                searchString = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        noResultPlaceholder = findViewById<LinearLayout>(R.id.noResultsPlaceholder)
        noNetworkPlaceholder = findViewById<LinearLayout>(R.id.noNetworkPlaceholder)
        val refreshButton = findViewById<Button>(R.id.renew_button)
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

        clearButton.setOnClickListener {
            editText.text?.clear()
            songs.clear()
            trackAdapter.notifyDataSetChanged()
            noResultPlaceholder.visibility = View.GONE
            noNetworkPlaceholder.visibility = View.GONE
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            editText.clearFocus()
        }

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerViewHistory = findViewById<RecyclerView>(R.id.history_recycler)
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter
        recyclerViewHistory.adapter = historyAdapter

        refreshButton.setOnClickListener {
            noNetworkPlaceholder.visibility = View.GONE
            lastSearch?.let { query -> performSearch(query) }
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                noResultPlaceholder.visibility = View.GONE
                noNetworkPlaceholder.visibility = View.GONE
                val query = editText.text.toString()
                lastSearch = query
                performSearch(query)

                true
            }
            false
        }
    }

    private fun openPlayer(song: Song) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(EXTRA_TRACK, song)
        startActivity(intent)
    }

    private fun renderState(state: SearchUIState) {
        when (state) {
            is SearchUIState.Initial -> {
                recyclerView.visibility = View.GONE
                historyHint.visibility = View.GONE
                noResultPlaceholder.visibility = View.GONE
                noNetworkPlaceholder.visibility = View.GONE
            }

            is SearchUIState.ShowHistory -> {
                recyclerView.visibility = View.GONE
                noResultPlaceholder.visibility = View.GONE
                noNetworkPlaceholder.visibility = View.GONE
                historyHint.visibility = View.VISIBLE
                historyAdapter.updateData(searchHistory.getHistory())
            }

            is SearchUIState.ShowEmptyPlaceholder -> {
                recyclerView.visibility = View.GONE
                historyHint.visibility = View.GONE
                noResultPlaceholder.visibility = View.VISIBLE
                noNetworkPlaceholder.visibility = View.GONE
            }

            is SearchUIState.ShowNoNetworkPlaceholder -> {
                recyclerView.visibility = View.GONE
                historyHint.visibility = View.GONE
                noResultPlaceholder.visibility = View.GONE
                noNetworkPlaceholder.visibility = View.VISIBLE
            }

            is SearchUIState.ShowSearchResults -> {
                recyclerView.visibility = View.VISIBLE
                historyHint.visibility = View.GONE
                noResultPlaceholder.visibility = View.GONE
                noNetworkPlaceholder.visibility = View.GONE
                historyAdapter.updateData(songs)
            }

            is SearchUIState.Typing -> {
                recyclerView.visibility = View.GONE
                historyHint.visibility = View.GONE
                noResultPlaceholder.visibility = View.GONE
                noNetworkPlaceholder.visibility = View.GONE
            }

        }
    }

    private fun performSearch(query: String) {
        progressBar.visibility = View.VISIBLE
        noNetworkPlaceholder.visibility = View.GONE
        noResultPlaceholder.visibility = View.GONE
        recyclerView.visibility = View.GONE
        interactor.searchSongs(query, object : SongsInteractor.SongsConsumer {
            override fun consume(foundSongs: List<Song>) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    songs.clear()
                    songs.addAll(foundSongs)
                    trackAdapter.notifyDataSetChanged()

                    if (songs.isEmpty()) {
                        noResultPlaceholder.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        noResultPlaceholder.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
            }

        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val editText = findViewById<EditText>(R.id.search_edittext)
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, editText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val editText = findViewById<EditText>(R.id.search_edittext)
        searchString = savedInstanceState.getString(SEARCH_STRING, STRING_DEF)
        editText.setText(searchString)
    }

    private var isClickAllowed = true
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (current) {
            isClickAllowed = false
            searchHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private val SEARCH_DEBOUNCE_DELAY = 2000L
        private val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_STRING = "SEARCH_STRING"
        private const val STRING_DEF = ""
        const val EXTRA_TRACK = "EXTRA_TRACK"
    }
}