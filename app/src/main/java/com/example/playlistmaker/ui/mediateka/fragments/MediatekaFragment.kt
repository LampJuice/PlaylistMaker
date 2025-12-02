package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekaBinding
import com.example.playlistmaker.ui.mediateka.fragments.favorite.FavoriteFragment
import com.example.playlistmaker.ui.search.models.SongUi
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class MediatekaFragment : Fragment(), FavoriteFragment.Listener {

    private val gson: Gson by inject()
    private var _binding: FragmentMediatekaBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediatekaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediatekaViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_favorites)
                else -> getString(R.string.tab_playlists)
            }
        }
        tabMediator.attach()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tabMediator.detach()
    }

    override fun onTrackClicked(song: SongUi) {
        val bundle = bundleOf(FavoriteFragment.EXTRA_TRACK to gson.toJson(song))
        findNavController().navigate(R.id.action_mediatekaFragment2_to_playerFragment2, bundle)
    }

}