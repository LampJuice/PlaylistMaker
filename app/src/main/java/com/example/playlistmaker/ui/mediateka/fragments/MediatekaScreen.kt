package com.example.playlistmaker.ui.mediateka.fragments

import android.view.LayoutInflater
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.compose.ScreenTitle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@Composable
fun MediatekaScreen(fragment: MediatekaFragment) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_menu))

    ) {
        Row(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.main_padding)))
        { ScreenTitle(title = stringResource(R.string.media)) }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val root = LayoutInflater.from(context)
                    .inflate(R.layout.fragment_mediateka, null, false)

                val viewPager = root.findViewById<ViewPager2>(R.id.viewPager)
                val tabLayout = root.findViewById<TabLayout>(R.id.tabLayout)

                viewPager.adapter =
                    MediatekaViewPagerAdapter(
                        fragment.childFragmentManager,
                        fragment.lifecycle
                    )

                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = when (position) {
                        0 -> context.getString(R.string.tab_favorites)
                        else -> context.getString(R.string.tab_playlists)
                    }

                }.attach()
                root
            }
        )
    }
}