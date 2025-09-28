package com.example.playlistmaker.ui.search.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.ui.search.models.SongUi
import com.example.playlistmaker.ui.search.view_model.SearchUIState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()
    private val gson: Gson by inject()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = trackAdapter

        binding.historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecycler.adapter = historyAdapter

        viewModel.stateSearchLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }
        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }
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

        when (state) {
            is SearchUIState.Initial -> Unit
            is SearchUIState.Loading -> searchProgressBar.visibility = View.VISIBLE
            is SearchUIState.ShowEmptyPlaceholder -> noResultsPlaceholder.visibility = View.VISIBLE
            is SearchUIState.ShowHistory -> {
                historyAdapter.updateData(state.history)
                historyHint.visibility = View.VISIBLE
            }

            is SearchUIState.ShowNoNetworkPlaceholder -> noNetworkPlaceholder.visibility =
                View.VISIBLE

            is SearchUIState.ShowSearchResults -> {
                trackAdapter.updateData(state.results)
                recyclerView.visibility = View.VISIBLE
            }

            is SearchUIState.Typing -> Unit
            SearchUIState.ClearInput -> {
                searchEdittext.text?.clear()
                searchEdittext.clearFocus()
                val inputMethodManager =
                    requireActivity().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(searchEdittext.windowToken, 0)
            }
        }
    }

    private fun openPlayer(song: SongUi) {
        val songJson = gson.toJson(song)
        val bundle = bundleOf(EXTRA_TRACK to songJson)
        findNavController().navigate(R.id.action_searchFragment2_to_playerFragment2, bundle)
    }

    companion object {
        const val EXTRA_TRACK = "EXTRA_TRACK"
    }
}