package com.example.playlistmaker.ui.mediateka.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistsState
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel()
    private val adapter = PlaylistsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlaylists()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter


        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsState.Empty -> {
                    with(binding) {
                        recyclerView.isVisible = false
                        playlistEmpty.isVisible = true
                    }

                }

                is PlaylistsState.Content -> {
                    with(binding) {
                        recyclerView.isVisible = true
                        playlistEmpty.isVisible = false
                    }
                    with(adapter) {
                        items = state.playlists
                        notifyDataSetChanged()
                    }
                }
            }

        }

        binding.newPlaylistButton.setOnClickListener {
            val navController = requireActivity()
                .supportFragmentManager
                .findFragmentById(R.id.rootFragmentContainerView)
                ?.findNavController()
            navController?.navigate(R.id.action_mediatekaFragment2_to_createPlaylistFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance(): PlaylistsFragment = PlaylistsFragment()
    }
}