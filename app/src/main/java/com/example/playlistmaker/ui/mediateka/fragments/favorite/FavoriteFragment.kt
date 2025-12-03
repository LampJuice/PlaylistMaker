package com.example.playlistmaker.ui.mediateka.fragments.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.ui.mediateka.view_model.FavoriteViewModel
import com.example.playlistmaker.ui.mediateka.view_model.FavoritesUiState
import com.example.playlistmaker.ui.search.fragment.TrackAdapter
import com.example.playlistmaker.ui.search.models.SongUi
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    interface Listener {
        fun onTrackClicked(song: SongUi)
    }

    private var listener: Listener? = null

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModel()

    private val favoritesAdapter = TrackAdapter(
        mutableListOf(), { song -> listener?.onTrackClicked(song) }, { song -> Unit }
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? Listener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = favoritesAdapter

        viewModel.showFavorites()

        viewModel.stateFavoritesLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }

    }

    private fun render(state: FavoritesUiState) = with(binding) {
        recyclerView.visibility = View.GONE
        mediatekaIsEmpty.visibility = View.GONE
        when (state) {
            FavoritesUiState.ShowEmptyPlaceholder -> mediatekaIsEmpty.visibility = View.VISIBLE

            is FavoritesUiState.ShowFavorites -> {


                favoritesAdapter.updateData(state.favorites)
                recyclerView.visibility = View.VISIBLE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): FavoriteFragment = FavoriteFragment()
        const val EXTRA_TRACK = "EXTRA_TRACK"
    }
}