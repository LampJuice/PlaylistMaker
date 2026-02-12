package com.example.playlistmaker.ui.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.search.models.SongUi
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()
    private val gson: Gson by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SearchScreen(
                    viewModel = viewModel,
                    onTrackClick = { song ->
                        if (viewModel.onTrackClick(song)) {
                            openPlayer(song)
                        }
                    }
                )
            }
        }
    }


    private fun openPlayer(song: SongUi) {
        val songJson = gson.toJson(song)
        val bundle = bundleOf(EXTRA_TRACK to songJson)
        val navController = requireActivity()
            .supportFragmentManager
            .findFragmentById(R.id.rootFragmentContainerView)!!
            .findNavController()
        navController.navigate(R.id.action_searchFragment2_to_playerFragment2, bundle)
    }

    companion object {
        const val EXTRA_TRACK = "EXTRA_TRACK"
    }


}