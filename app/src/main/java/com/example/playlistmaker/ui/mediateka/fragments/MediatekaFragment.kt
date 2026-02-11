package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.mediateka.fragments.favorite.FavoriteFragment
import com.example.playlistmaker.ui.mediateka.fragments.playlists.PlaylistsFragment
import com.example.playlistmaker.ui.search.models.SongUi
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class MediatekaFragment : Fragment(), FavoriteFragment.Listener, PlaylistsFragment.Listener {

    private val gson: Gson by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MediatekaScreen(fragment = this@MediatekaFragment)
            }
        }
    }


    override fun onTrackClicked(song: SongUi) {
        val bundle = bundleOf(FavoriteFragment.EXTRA_TRACK to gson.toJson(song))
        findNavController().navigate(R.id.action_mediatekaFragment2_to_playerFragment2, bundle)
    }

    override fun onPlaylistClicked(playlist: Playlist) {
        val bundle = bundleOf(PlaylistsFragment.Companion.EXTRA_PLAYLIST to playlist.id)
        findNavController().navigate(R.id.action_mediatekaFragment2_to_playlistScrFragment, bundle)

    }


}