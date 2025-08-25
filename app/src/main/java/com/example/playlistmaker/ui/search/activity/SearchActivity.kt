package com.example.playlistmaker.ui.search.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.models.Song
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchUIState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel

    private val trackAdapter = TrackAdapter(mutableListOf()) { song ->
        if (viewModel.onTrackClick(song)) {
            openPlayer(song)
        }
    }
    private val historyAdapter =
        TrackAdapter(mutableListOf()) { song ->
            if (viewModel.onTrackClick(song)) {
                openPlayer(song)
            }
        }

    private lateinit var binding: ActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getFactory(
                Creator.provideSongsInteractor(),
                Creator.provideSearchHistoryInteractor()
            )
        )[SearchViewModel::class.java]

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = trackAdapter

        binding.historyRecycler.layoutManager = LinearLayoutManager(this)
        binding.historyRecycler.adapter = historyAdapter

        viewModel.stateSearchLiveData.observe(this) { state ->
            render(state)
        }





        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.searchBack.setOnClickListener { finish() }

        binding.searchEdittext.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onFocusGained(binding.searchEdittext.text.toString(), hasFocus)
        }

        binding.searchEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchTextChanged(s.toString(), binding.searchEdittext.hasFocus())
                binding.clearText.visibility =
                    if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        binding.clearText.setOnClickListener {
            viewModel.clearEditText()
        }


        binding.renewButton.setOnClickListener {
            viewModel.onRenewClick()

        }

    }

    private fun render(state: SearchUIState) = with(binding) {
        recyclerView.visibility = View.GONE
        historyHint.visibility = View.GONE
        noResultsPlaceholder.visibility = View.GONE
        noNetworkPlaceholder.visibility = View.GONE
        searchProgressBar.visibility = View.GONE

        when(state){
            is SearchUIState.Initial -> Unit
            is SearchUIState.Loading -> searchProgressBar.visibility = View.VISIBLE
            is SearchUIState.ShowEmptyPlaceholder -> noResultsPlaceholder.visibility = View.VISIBLE
            is SearchUIState.ShowHistory -> {
                historyAdapter.updateData(state.history)
                historyHint.visibility = View.VISIBLE
            }
            is SearchUIState.ShowNoNetworkPlaceholder -> noNetworkPlaceholder.visibility = View.VISIBLE
            is SearchUIState.ShowSearchResults -> {
                trackAdapter.updateData(state.results)
                recyclerView.visibility = View.VISIBLE
            }
            is SearchUIState.Typing -> Unit
            SearchUIState.ClearInput -> {
                searchEdittext.text?.clear()
                searchEdittext.clearFocus()
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(searchEdittext.windowToken, 0)
            }
        }
    }



    private fun openPlayer(song: Song) {
        val songJson = Gson().toJson(song)
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(EXTRA_TRACK, songJson)
        startActivity(intent)
    }


    companion object {

        const val EXTRA_TRACK = "EXTRA_TRACK"
    }
}