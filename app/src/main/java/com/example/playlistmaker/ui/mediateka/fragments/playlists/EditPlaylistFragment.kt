package com.example.playlistmaker.ui.mediateka.fragments.playlists

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.mediateka.fragments.CreatePlaylistFragment
import com.example.playlistmaker.ui.mediateka.view_model.EditPlaylistViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {

    override val viewModel: EditPlaylistViewModel by viewModel()
    private val gson: Gson by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistJson = arguments?.getString(PlaylistsFragment.Companion.EXTRA_PLAYLIST)
        playlistJson?.let { json ->
            lifecycleScope.launch(Dispatchers.Default) {
                try {
                    val playlist = gson.fromJson(json, Playlist::class.java)
                    withContext(Dispatchers.Main) {
                        viewModel.initData(playlist)
                        binding.newPlaylist.setText(R.string.edit_playlist_new)
                        binding.btnCreatePl.setText(R.string.save)
                        binding.plName.setText(playlist.name)
                        binding.plDesc.setText(playlist.description ?: "")
                        playlist.coverPath?.let { binding.coverImg.setImageURI(it.toUri()) }

                        binding.btnCreatePl.setOnClickListener {
                            viewModel.savePlaylist { findNavController().popBackStack() }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        viewModel.initData(
                            Playlist(
                                0, "", "", null,
                                songIds = emptyList(),
                                songCount = 0
                            )
                        )
                    }
                }
            }
        } ?: run {
            viewModel.initData(
                Playlist(
                    0, "", "", null,
                    songIds = emptyList(),
                    songCount = 0
                )
            )
        }
    }

    override fun onBackPressed() {
        findNavController().popBackStack()
    }


}