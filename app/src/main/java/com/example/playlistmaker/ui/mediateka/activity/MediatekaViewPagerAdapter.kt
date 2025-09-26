package com.example.playlistmaker.ui.mediateka.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.mediateka.fragments.FavoriteFragment
import com.example.playlistmaker.ui.mediateka.fragments.PlaylistsFragment

class MediatekaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteFragment.Companion.newInstance()
            else -> PlaylistsFragment.Companion.newInstance()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}